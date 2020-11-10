class Navigator {

  constructor() {
    this.FOUND_STATE = "FOUND";
    this.screenLock = false;
    
    this.absoluteAngle = 0;
    this.activeAngle = 0;

    this.targetStatus = null;
    this.target = null;

    this.orientationAbsolute = false; // remove this variable as it is not really used
    this.orientationCurrent = 0;

    this.compass = null;
    this.orientationOffset = 0;

    this.accuracy = 0;
    this.accuracyHistory = [];

    this.isFiltering = false;
    
    this.heartBeatId =  null;
    
    this.lastUpdate = Date.now()-1000;
    
    this.geoLocationOptions = {
      enableHighAccuracy: true,
      timeout: 2000
    };
    
    const navElement = document.getElementById('nav-element');
    navElement.addEventListener('dblclick', this.continue.bind(this));

    const permissionElement = document.getElementById('permission-element');
    permissionElement.addEventListener('click', this.handlePermission.bind(this));

    const navStatus = document.getElementById('nav-status');
    navStatus.addEventListener('input', this.filterEmojiInput.bind(this));

    const status = document.getElementById('status-element');
    status.innerText = '';
  }

  filterEmojiInput(e) {
    // lock to avoid recursive calls
    if (this.isFiltering) {return;}
    this.isFiltering = true;
    
    const input = e.target.value;
    // https://unicode.org/reports/tr51/#Emoji_Properties
    const regexpEmojiPresentation = /\p{Emoji}/gu;
    let filteredInput = "";
    const matches = input.match(regexpEmojiPresentation);
    if (matches) {
        filteredInput = matches.join('');
    }
    
    // simple update of the target value does not seem to work
    // slight workaround
    e.target.setRangeText(filteredInput, 0, e.target.value.length, "end");
    
    // release lock
    this.isFiltering = false;
  }

  handlePermission() {
    let that = this;

    // this needs to be triggered by a real user interaction
    // therefore we always show the satellite at the beginning
    if (this.screenLock === false) {
      var noSleep = new NoSleep();
      noSleep.enable();
      this.screenLock = true;
    }

    const PROBABLY_GRANTED=50;
    if (navigator.permissions === undefined) {
        this.verifyPermission(PROBABLY_GRANTED, that);
    } else {
      navigator.permissions.query({name:'geolocation'}).then(function(result) {
        if (result.state == 'granted') {
          console.log("GEO PERMISSION already enabled - STARTING APP");
          that.startNavigation();
        } else if (result.state == 'prompt') {
          that.verifyPermission(PROBABLY_GRANTED, that);
        } else if (result.state == 'denied') {
          // TODO show a geo permission denied emoji
          console.log("THIS WONT WORK WITHOUT GEO PERMISSIONS");
        }
        result.onchange = function() {
          if (result.state == 'granted') {
            console.log("GEO PERMISSION permanently enabled - STARTING APP");
            that.startNavigation();
          }
        }
      });
    }
  }
  
  verifyPermission(PROBABLY_GRANTED, that) {
      const beforePermissionQuestion = Date.now();
      // this should just trigger the permission question
      // if the response is extremly fast we can assume it was accepted permanently
      // and is actually in state granted
      // looks like a bug in firefox
      // https://stackoverflow.com/questions/55127053/navigator-permissions-query-geolocation-onchange-not-working-in-firefox
      navigator.geolocation.getCurrentPosition(function() {
          const afterPermissionQuestion = Date.now();
          const diff = (afterPermissionQuestion - beforePermissionQuestion);
          console.log(diff);
          if (diff < PROBABLY_GRANTED) {
              that.startNavigation();
          }
      });
  }

  hidePermission() {
    document.querySelector('.permission-container').classList.add('-hidden');
  }

  startNavigation() {
    this.hidePermission();
    document.querySelector('.nav-container').classList.remove('-hidden');
    this.start();
  }

  pauseNavigation() {
    this.pause();
  }

  refreshOffset(summary) {
    this.orientationOffset = summary.northOffset;
  }

  handleOrientation(evt) {
    this.orientationAbsolute = evt.absolute;
    if (this.orientationAbsolute) { 
        this.orientationCurrent = evt.alpha; 
    } else if (evt.hasOwnProperty('webkitCompassHeading')) { 
        //get absolute orientation for Safari/iOS
        this.orientationAbsolute = true;
        this.orientationCurrent = 360 - event.webkitCompassHeading; // TODO this is not tested
    } else {
        if (this.compass === null) {
            this.compass = new Compass();
            this.compass.start();
            this.compass.register(this.refreshOffset.bind(this));
        }
        this.orientationAbsolute = false;
        this.orientationCurrent = evt.alpha;
    }
  }

  updateDebug() {
    const debug = document.getElementById('status-element');
    if (this.target) {
        debug.innerText = `${this.target.geo_distance}m ± ${this.accuracy}m`;
    }
    if (this.compass != null) {
        debug.innerText += ` ± ${this.orientationOffset}deg`;
    }
  }

  start() {
    if ('ondeviceorientationabsolute' in window) {
        // works only in Chrome
        window.addEventListener('deviceorientationabsolute', this.handleOrientation.bind(this));
    } else if ('ondeviceorientation' in window) {
        window.addEventListener('deviceorientation', this.handleOrientation.bind(this));
    }
    this.heartBeatId = navigator.geolocation.watchPosition(this.updateCoordinates.bind(this), this.noGeoPositionAvailable.bind(this), this.geoLocationOptions);
    this.navigationInterval = setInterval(this.updateNavigation.bind(this), 100);
  }

  continue() {
    if (this.searchState === this.FOUND_STATE) {
        this.searchState = null;
        this.heartBeatId = navigator.geolocation.watchPosition(this.updateCoordinates.bind(this), this.noGeoPositionAvailable.bind(this), this.geoLocationOptions);
    }
  }

  pause() {
    navigator.geolocation.clearWatch(this.heartBeatId);
  }
  

  updateCoordinates(position) {
    const navigationStatus = document.getElementById('nav-status');

    const latitude = position.coords.latitude;
    const longitude = position.coords.longitude;
    this.accuracy = Math.round(position.coords.accuracy);
    this.accuracyHistory.push(this.accuracy < 30);
    const status = navigationStatus.value
    const url = `/point/${this.sessionId}`;
    const contentType = "application/json;charset=UTF-8";
    const params = { "direction": 0, "latitude": latitude, "longitude": longitude, "status": status, "accuracy": this.accuracy};

    this.updateDebug();
    
    // rate limit position update
    if ((Date.now() - this.lastUpdate) < 1000) {
        return;
    }
    this.lastUpdate = Date.now();

    Util.post(url, contentType, params)
      .then((response) => {
        const direction = JSON.parse(response);

        this.absoluteAngle = direction.angle;
        this.targetStatus = direction.status;
        this.target = direction.target;
        this.searchState = direction.searchState
        
        this.updateNavigation();
        this.updateDebug();

        if (direction.searchState === this.FOUND_STATE) {
            this.pauseNavigation();
        }
      })
      .catch((error) => {
        console.log(error);
        this.target = null;
        this.targetStatus = null;
        this.updateNavigation();
      });
  }

  updateNavigation() {
    const navigationElement = document.getElementById('nav-element');
    const navigationTarget = document.getElementById('nav-target');
    const statusElement = document.getElementById('status-container');

    if (this.searchState === this.FOUND_STATE) {
        document.querySelector('body').classList.add('-found');
        navigationElement.innerText = "🏁";
        navigationElement.style.transform = '';
        return;
    } else {
        document.querySelector('body').classList.remove('-found');
    }

    if (this.targetStatus !== null) {
        statusElement.classList.add('-active-position');
        navigationTarget.classList.remove('-inactive');
        navigationTarget.innerText = this.targetStatus;
    } else {
        statusElement.classList.remove('-active-position');
        navigationTarget.classList.add('-inactive');
        navigationTarget.innerText = "🎯";
    }
    
    if (this.isAccurate()) {
        navigationElement.classList.remove('-blur');
        navigationElement.innerText = "^";
        navigationElement.style.transform = `rotate(${this.relativeAngle}deg)`;
    } else {
        navigationElement.classList.add('-blur');
        navigationElement.innerText = "⚠️";
        navigationElement.style.transform = '';
    }

  }
  
  isAccurate() {
    // reduce array to the last 10 elements
    // TODO this should be done in a setter 
    this.accuracyHistory = this.accuracyHistory.slice(-10);
    
    const totalPoints = this.accuracyHistory.length;
    if (totalPoints < 3) {
        return true;
    }

    const accuratePoints = this.accuracyHistory.filter(point => point).length;
    if ((accuratePoints/totalPoints) > 0.7) return true;
    return false;
  }

  noGeoPositionAvailable(err) {
    console.warn(`error: ${err.message}`);
    document.getElementById('status-container').classList.remove('-active-position');
  }

  get sessionId() {
    let sessionId = document.cookie.replace(/(?:(?:^|.*;\s*)sessionId\s*\=\s*([^;]*).*$)|^.*$/, "$1");
    if (sessionId === "") {
      sessionId = Util.generateUUID();
      document.cookie = `sessionId=${sessionId};max-age=86400`;
    }
    return sessionId;
  }

  get relativeAngle() {
    return this.orientiedAngle;
  }

  get orientiedAngle() {
    return (360 + (this.absoluteAngle + this.orientationCurrent - this.orientationOffset))%360;
  }

}

class Util {
  static generateUUID() { // Public Domain/MIT
    var d = new Date().getTime();//Timestamp
    var d2 = (performance && performance.now && (performance.now() * 1000)) || 0;//Time in microseconds since page-load or 0 if unsupported
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      var r = Math.random() * 16;//random number between 0 and 16
      if (d > 0) {//Use timestamp until depleted
        r = (d + r) % 16 | 0;
        d = Math.floor(d / 16);
      } else {//Use microseconds since page-load if supported
        r = (d2 + r) % 16 | 0;
        d2 = Math.floor(d2 / 16);
      }
      return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
  }

  static post(url, contentType, params) {
    let promise = new Promise((resolve, reject) => {
      const xmlhttp = new XMLHttpRequest();
      xmlhttp.onload = () => {
        if (xmlhttp.readyState === 4) {
          if (xmlhttp.status === 200) {
            resolve(xmlhttp.response);
          } else {
            // e.g. no other participant (Status 204)
            if (xmlhttp.status >= 400) {
              console.error(xmlhttp);
            }
            reject(xmlhttp);
          }
        }
      };
      xmlhttp.open("POST", url);
      xmlhttp.setRequestHeader("Content-Type", contentType);
      xmlhttp.send(JSON.stringify(params));
    });
    return promise;
  }
}

document.addEventListener('DOMContentLoaded', function (_evt) {
  const navigator = new Navigator();
});
