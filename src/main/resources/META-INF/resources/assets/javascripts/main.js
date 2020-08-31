class Navigator {

  constructor() {
    this.FOUND_STATE = "FOUND";
    this.screenLock = false;
    
    this.absoluteAngle = 0;
    this.activeAngle = 0;
    
    this.orientationAbsolute = false;
    this.orientationOffset = null;
    this.orientationCurrent = 0;
    
    this.geoLocationOptions = {
      enableHighAccuracy: true,
      timeout: 1000
    };
    
    const setupElement = document.getElementById('setup-element');

    setupElement.addEventListener('dblclick', this.skipSetup.bind(this));

    const permissionElement = document.getElementById('permission-element');

    permissionElement.addEventListener('click', this.handlePermission.bind(this));
  }

  handlePermission() {
    let that = this;
    if (this.screenLock === false) {
      var noSleep = new NoSleep();
      noSleep.enable();
      this.screenLock = true;
    }

    navigator.permissions.query({name:'geolocation'}).then(function(result) {
      if (result.state == 'granted') {
        console.log("GEO PERMISSION already enabled - STARTING APP");
        that.startSetup();
      } else if (result.state == 'prompt') {
        const beforePermissionQuestion = Date.now();
        // this should just trigger the permission question
        // if the response is extremly fast we can assume it was accepted automatically and is granted
        // looks like a bug
        navigator.geolocation.getCurrentPosition(function() {
            const afterPermissionQuestion = Date.now();
            const PROBABLY_GRANTED=50;
            const diff=(afterPermissionQuestion-beforePermissionQuestion);
            console.log(diff);
            if (diff < PROBABLY_GRANTED) {
                that.startSetup();
            }
            }, function() {;});
      } else if (result.state == 'denied') {
        // TODO show a geo permission denied emoji
        console.log("THIS WONT WORK WITHOUT GEO PERMISSIONS");
      }
      result.onchange = function() {
        if (result.state == 'granted') {
          console.log("GEO PERMISSION permanently enabled - STARTING APP");
          that.startSetup();
        }
      }
    });
  }
  
  skipSetup() {
    this.updateSetup();
  }

  refreshOffset(summary) {
    const oldOrientationOffset = this.orientationOffset;
    this.orientationOffset = summary.northOffset;
    if (oldOrientationOffset === null) { this.updateSetup(); }
  }

  startSetup() {
    this.hidePermission();
    this.showSetup();

    const compass = new Compass();
    compass.start();
    compass.register(this.refreshOffset.bind(this));
  }

  updateSetup() {
//    if (this.orientationAbsolute === false) {
//      this.orientationOffset = this.orientationCurrent;
//    }
    this.hideSetup();
    this.showNavigation();
  }

  hidePermission() {
    document.querySelector('.permission-container').classList.add('-hidden');
  }

  showSetup() {
    document.querySelector('.setup-container').classList.remove('-hidden');
  }

  hideSetup() {
    document.querySelector('.setup-container').classList.add('-hidden');
  }

  showNavigation() {
    document.querySelector('.nav-container').classList.remove('-hidden');
    this.start();
  }

  stopNavigation() {
    document.querySelector('.nav-container').classList.add('-hidden');
    this.stop();
  }

  showDestination() {
    document.querySelector('.destination-container').classList.remove('-hidden');
    document.querySelector('body').classList.add('-found');
  }

  handleOrientation(evt) {
    this.orientationAbsolute = evt.absolute;
    this.orientationCurrent = evt.alpha;
    if (this.orientationOffset == null) {
        console.log(`INIT MANUALLY ORIENTATION OFFSET: ${this.orientationCurrent}`)
        this.orientationOffset = this.orientationCurrent;
    }
  }

  updateDebug(target) {
    const debug = document.getElementById('debug-container');
    const output = JSON.stringify(target, null, 4);
    debug.innerText = `target: ${output}`;
  }

  start() {
    window.addEventListener("deviceorientation", this.handleOrientation.bind(this), true);
    this.heartBeatInterval = setInterval(this.heartbeat.bind(this), 2000);
    this.navigationInterval = setInterval(this.updateNavigation.bind(this), 100);
  }

  stop() {
    clearInterval(this.heartBeatInterval);
  }

  heartbeat() {
    window.navigator.geolocation.getCurrentPosition(this.updateCoordinates.bind(this), this.noGeoPositionAvailable.bind(this), this.geoLocationOptions);
  }

  updateCoordinates(position) {
    const latitude = position.coords.latitude;
    const longitude = position.coords.longitude;
    const url = `/point/${this.sessionId}`;
    const contentType = "application/json;charset=UTF-8";
    const params = { "direction": 0, "latitude": latitude, "longitude": longitude };

    Util.post(url, contentType, params)
      .then((response) => {
        const div = document.getElementById('status-container');
        const direction = JSON.parse(response);
        div.classList.add('-active-position');

        this.absoluteAngle = direction.angle;
        this.updateNavigation();
        if (direction.target) {
            this.updateDebug(direction.target);
        }

        if (direction.searchState === this.FOUND_STATE) {
            this.showDestination();
            this.stopNavigation();
        }
      })
      .catch((error) => console.log(error));
  }

  updateNavigation() {
    const navigationElement = document.getElementById('nav-element');

    navigationElement.style.transform = `rotate(${this.relativeAngle}deg)`;
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
    return (360 + (this.absoluteAngle - this.northedOrientation))%360;
    //return this.absoluteAngle;
  }
  
  get northedOrientation() {
    let diff = this.orientationOffset-this.orientationCurrent;
    if (diff < 0) {
      diff = 360 - Math.abs(diff);
    }
    return Math.floor(diff);
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
          } else if (xmlhttp.status >= 400) {
            // e.g. no other participant (Status 204)
            console.error(xmlhttp.statusText);
            reject(xmlhttp.statusText);
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
