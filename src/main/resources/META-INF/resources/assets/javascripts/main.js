class Navigator {

  constructor() {
    // TODO how to do enums
    this.FOUND_STATE = "FOUND";
    this.SEARCHING_STATE = "SEARCHING";
    this.screenLock = false;
    
    this.activeAngle = 0;

    this.navState = {angle:0, target: {status: null}, searchState: this.SEARCHING_STATE};
    this.searchState = this.SEARCHING_STATE;

    this.orientationAbsolute = false; // remove this variable as it is not really used
    this.orientationCurrent = 0;

    this.compass = null;
    this.orientationOffset = 0;

    this.accuracy = 0;
    this.accuracyHistory = [];

    this.latitude = 0.0;
    this.longitude = 0.0;

    this.isFiltering = false;
    
    this.heartBeatId =  null;
    
    this.lastUpdate = Date.now()-1000;

    this.orientationEventAllowed = false;
    
    this.geoLocationOptions = {
      enableHighAccuracy: true,
      timeout: 2000
    };
    
    const navElement = document.getElementById('nav-element');
    navElement.addEventListener('dblclick', this.continue.bind(this));

    const legalVerified = document.cookie.replace(/(?:(?:^|.*;\s*)legal-verified\s*\=\s*([^;]*).*$)|^.*$/, "$1");
    const legalStatus = document.getElementById('legal');
    if (legalVerified === "true") {
      legalStatus.checked = true;
      const legalContainer = document.getElementById('legal-element');
      legalContainer.classList.add('-hidden');
    } else {
      legalStatus.checked = false;
    }

    const permissionElement = document.getElementById('permission-container');
    permissionElement.addEventListener('click', this.handlePermission.bind(this));

    const navStatus = document.getElementById('nav-status-input');
    navStatus.addEventListener('input', this.filterEmojiInput.bind(this));

    const uploadElement = document.getElementById('upload-file-target');
    uploadElement.addEventListener('click', this.uploadTrigger.bind(this));
    const upload = document.getElementById('upload-file-input');
    upload.addEventListener('change', this.uploadFile.bind(this));

    const accountDeletionElement = document.getElementById('account-deletion-element');
    accountDeletionElement.addEventListener('click', this.deleteAccount.bind(this));

    const switches = document.querySelectorAll(".switch-screen");
    for (let s of switches) {
      s.addEventListener('click', this.switchScreen.bind(this));
    }

    this.screenRefresh = [
      {screen: 'personal', method: this.refreshPersonal.bind(this)},
      {screen: 'points', method: this.refreshPoints.bind(this)}
    ];

    const debug = document.getElementById('debug-element');
    debug.innerText = '';
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

  switchScreen(evt) {
    const screen = evt.target.dataset['switchTarget'];
    const screens = document.querySelectorAll(".screen");
    for (let s of screens) {
      if (s.id.includes(screen)) {
        s.classList.remove('-hidden');
      } else {
        s.classList.add('-hidden');
      }
    }

    this.refresh(screen);
  }

  refresh(screen) {
    for (let r of this.screenRefresh) {
      if (r.screen === screen) {
        r.method();
      }
    }
  }

  refreshPersonal() {
    const url = '/personal';
    Util.get(url)
    .then((response) => {
      this.renderPersonalScreen(JSON.parse(response));
    })
    .catch((error) => {
      console.log(error);
      this.renderPersonalScreen(null);
    });

    const personalElement = document.getElementById('personal-refcode');
    personalElement.innerHTML = "⏳";

    const accountDeletion = document.getElementById('account-deletion');
    accountDeletion.checked = false;
  }

  renderPersonalScreen(data) {
    const personalElement = document.getElementById('personal-refcode');
    personalElement.innerHTML = " ";

    const personalContainer = document.getElementById('personal-container');
    personalContainer.innerHTML = `<div class="heading">📊</div><div>👣${data.trajectoryPoints}</div><div>🗺️${data.pointsPoints}</div><div>🚦${data.ratingsPoints}</div>`;

    if (data.refCode !== null) {
      personalElement.innerHTML = "🆔 " + data.refCode.substring(0, 8);
    }
  }

  refreshPoints() {
    const url = '/points/list';
    const contentType = "application/json;charset=UTF-8";
    const params = {
      "uuid": this.sessionId,
      "location": {
        "lat": this.latitude,
        "lon": this.longitude
      }
    };

    Util.post(url, contentType, params)
    .then((response) => {
      this.renderPointsScreen(JSON.parse(response));
    })
    .catch((error) => {
      console.log(error);
      this.renderPointsScreen(null);
    });

    const pointsStatus = document.getElementById('points-status');
    pointsStatus.innerHTML = "⏳";
  }

  renderPointsScreen(data) {
    const pointsStatus = document.getElementById('points-status');
    pointsStatus.innerHTML = '';

    const pointsContainer = document.getElementById('points-container');
    pointsContainer.innerHTML = `<div class="heading">🗺️</div>`
    if (data.points.length > 0) {
      let table = '<table>';
      for (let point of data.points) {
        table += `<tr class="poi" data-id="${point.uuid}"><td>${point.status}</td><td>${Math.round(point.distance)}m</td><td>${Util.mimeToEmoji(point.mimeType)}</td><td><a href="">❌</a></td></tr>`;
      }
      table += '</table>';
      pointsContainer.innerHTML += table;
    }

    const pois = document.querySelectorAll("#points-container .poi a");
    for (let p of pois) {
      p.addEventListener('click', this.deletePoi.bind(this));
    }
  }

  deletePoi(evt) {
    evt.preventDefault();

    const poi = evt.target.parentElement.parentElement.dataset.id;
    evt.target.parentElement.parentElement.remove();

    const url = '/points/delete';
    const contentType = "application/json;charset=UTF-8";
    const params = {
      "uuid": poi,
    };

    Util.post(url, contentType, params)
    .catch((error) => {
      console.log(error);
    });
  }

  handlePermission() {
    let that = this;

    const debug = document.getElementById('debug-element');
    debug.innerText = '▶️';

    const legalStatus = document.getElementById('legal');
    if (legalStatus.checked === true) {
      document.cookie = `legal-verified=true;max-age=7776000`;
      debug.innerText += '§✅';
    } else {
      document.cookie = `legal-verified=false;max-age=7776000`;
      debug.innerText += '§❓';
      return;
    }
    
    try {
      if (ScreenOrientation.lock("portrait-primary")) {
        debug.innerText += '🔄';
      } else {
        debug.innerText += '🙃';
      }
    } catch (e) {
      debug.innerText += '🙃';
    }

    // this needs to be triggered by a real user interaction
    // therefore we always show the satellite at the beginning
    if (this.screenLock === false) {
      if ('wakeLock' in navigator) {
        navigator.wakeLock.request('screen');
        debug.innerText += '💡';
      } else {
        var noSleep = new NoSleep();
        noSleep.enable();
        this.screenLock = true;
        debug.innerText += '😴';
      }
      debug.innerText += '🔒';
    }

    // this needs to be triggered by a real user interaction
    // therefore we always show the satellite at the beginning
    if (typeof DeviceOrientationEvent.requestPermission === 'function') {
      DeviceOrientationEvent.requestPermission()
      .then(response => {
        debug.innerText += '↔️';
        if (!this.orientationEventAllowed) {
          this.orientationEventAllowed = true;
          // retry because original run was canceled
          this.handlePermission();
        }
      })
      .catch(response => {
        debug.innerText += '💫';
      });
    } else {
      this.orientationEventAllowed = true;
    }
    // hobo solution
    // I dont know how to wait for the result of the above permission dialogue
    // and I dont want to have it displayed while also asked for the geo permission
    // so backing out here
    if (!this.orientationEventAllowed) {
      return;
    }

    const PROBABLY_GRANTED=70;
    if (navigator.permissions === undefined) {
      debug.innerText += '⚠️';
      this.verifyPermission(PROBABLY_GRANTED, that);
    } else {
      navigator.permissions.query({name:'geolocation'}).then(function(result) {
        if (result.state == 'granted') {
          debug.innerText += '✅';
          console.log("GEO PERMISSION already enabled - STARTING APP");
          debug.innerText += '🔼';
          that.startNavigation();
          debug.innerText += '⏺️';
        } else if (result.state == 'prompt') {
          debug.innerText += '❓';
          that.verifyPermission(PROBABLY_GRANTED, that);
        } else if (result.state == 'denied') {
          debug.innerText += '‼️';
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
      const debug = document.getElementById('debug-element');
      debug.innerText += '👀';
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
              debug.innerText += '🔼';
              that.startNavigation();
              debug.innerText += '⏺️';
          } else {
            debug.innerText += '🐢';
          }
      },
      function(error) {
        debug.innerText += '⛔';
        debug.innerText += error.code;
        debug.innerText += '🥬';
      });
  }

  hidePermission() {
    document.getElementById('permission-screen').classList.add('-hidden');
  }

  startNavigation() {
    this.hidePermission();
    document.getElementById('navigation-screen').classList.remove('-hidden');
    this.start();
  }

  refreshOffset(summary) {
    this.orientationOffset = Math.round(summary.northOffset);
  }

  handleOrientation(evt) {
    this.orientationAbsolute = evt.absolute;
    if (this.orientationAbsolute) { 
        this.orientationCurrent = evt.alpha;
    } else if ('webkitCompassHeading' in evt) { 
        //get absolute orientation for Safari/iOS
        this.orientationAbsolute = true;
        this.orientationCurrent = 360 - evt.webkitCompassHeading;
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
      const navigationStatus = document.getElementById('nav-status-input');
      const status = navigationStatus.value;
      const rating = document.querySelector('input[name="rating"]:checked');
      const refCode = this.navState.target.refCode;

      if (refCode === null || rating === null) {
        this.searchState = this.SEARCHING_STATE;
        return;
      }

      const url = '/rate';
      const contentType = "application/json;charset=UTF-8";
      const params = {
        "uuid": this.sessionId,
        "location": {
          "lat": this.latitude,
          "lon": this.longitude
        },
        "status": status,
        "rating": rating.value,
        "refCode": refCode
      };

      Util.post(url, contentType, params)
      .then((response) => {
        this.searchState = this.SEARCHING_STATE;
      })
      .catch((error) => {
        console.log(error);
        this.searchState = this.SEARCHING_STATE;
      });

      const navigationElement = document.getElementById('nav-element');
      navigationElement.innerHTML = "⏳";
    }
  }

  uploadTrigger() {
    const upload = document.getElementById('upload-file-input');
    upload.click();
  }

  uploadFile() {
    const upload = document.getElementById('upload-file-input');
    const uploadStatus = document.getElementById('upload-file-status');
    const navigationStatus = document.getElementById('nav-status-input');

    uploadStatus.innerText = '⏳';
    uploadStatus.classList.remove('-done');
    uploadStatus.classList.add('-running');

    const file = upload.files[0];
    const meta = {
      "creator": this.sessionId,
      "location": {
        "lat": this.latitude,
        "lon": this.longitude
      },
      "status": navigationStatus.value,
      "accuracy": this.accuracy,
      "fileName": file.name
    };

    var formData = new FormData();
    formData.append('file', file, file.name);
    formData.append('meta', JSON.stringify(meta));

    const url = '/upload';
    Util.upload(url, formData)
      .then((response) => {
        console.log("upload done");
        uploadStatus.innerText = '✅';
        uploadStatus.classList.remove('-running');
        uploadStatus.classList.add('-done');
      })
      .catch((error) => {
        console.log(error);
        uploadStatus.innerText = '💥';
        uploadStatus.classList.remove('-running');
        uploadStatus.classList.add('-done');
      });
  }

  deleteAccount() {
    const verified = document.querySelector('input[name="account-deletion"]:checked');

    if (verified === null) {
      return;
    }

    const url = '/delete-account';
    const contentType = "application/json;charset=UTF-8";
    const params = {
      "uuid": this.sessionId
    };

    Util.post(url, contentType, params)
      .then((response) => {
        // remove cookies
        document.cookie = 'sessionId=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        document.cookie = 'legal-verified=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        // reload page
        window.location.reload();
      })
      .catch((error) => {
        console.log(error);
      });
  }

  updateCoordinates(position) {
    const navigationStatus = document.getElementById('nav-status-input');

    this.latitude = position.coords.latitude;
    this.longitude = position.coords.longitude;
    this.accuracy = Math.round(position.coords.accuracy);
    this.accuracyHistory.push(this.accuracy < 30);
    const status = navigationStatus.value
    const url = '/refresh';
    const contentType = "application/json;charset=UTF-8";
    const params = {
      "uuid": this.sessionId,
      "location": {
        "lat": this.latitude,
        "lon": this.longitude
      },
      "status": status,
      "accuracy": this.accuracy
    };

    this.updateNavigation();
    
    // rate limit position update
    if ((Date.now() - this.lastUpdate) < 1000) {
        return;
    }
    this.lastUpdate = Date.now();

    Util.post(url, contentType, params)
      .then((response) => {
        this.navState = JSON.parse(response);
        if (!this.searchState || this.searchState === this.SEARCHING_STATE) {
            this.searchState = this.navState.searchState
        }
        
        this.updateNavigation();
      })
      .catch((error) => {
        console.log(error);
        this.navState = {angle:0, target: {status: null}, searchState: this.SEARCHING_STATE};
        this.updateNavigation();
      });
  }

  updateNavigation() {
    const navigationElement = document.getElementById('nav-element');
    const navigationTarget = document.getElementById('nav-target');
    const statusElement = document.getElementById('location-status-element');

    const ratingContainer = document.getElementById('rating-container');
    const uploadFileTarget = document.getElementById('upload-file-target');

    let debugText = "";
    if (this.navState.geo_distance !== undefined) {
      if (this.navState.geo_distance >= 0) {
        debugText += `${Math.round(this.navState.geo_distance)}m ± ${this.accuracy}m`;
      }
    } else {
      debugText += `± ${this.accuracy}m`;
    }
    if (this.compass != null) {
        debugText += ` / 🧭 ${this.orientationOffset}deg`;
    }
    statusElement.innerText = debugText;

    if (this.navState.target.status !== null) {
        navigationTarget.classList.remove('-inactive');
        navigationTarget.innerText = this.navState.target.status;
    } else {
        navigationTarget.classList.add('-inactive');
        navigationTarget.innerText = "🎯";
    }

    if (this.searchState === this.FOUND_STATE ) {
        if (!document.querySelector('body').classList.contains('-found')) {
          document.querySelector('body').classList.add('-found');
          ratingContainer.classList.remove('-hidden');
          uploadFileTarget.classList.add('-hidden');
          if (this.navState && this.navState.target.fileHash) {
            let fHash = this.navState.target.fileHash;
            let mimeType = this.navState.target.mimeType;
            switch (true) {
              case /image\//.test(mimeType):
                navigationElement.innerHTML = `<img class="media" src="/download/${fHash}">`;
                break;
              case /audio\//.test(mimeType):
                  navigationElement.innerHTML = `<audio class="media" controls src="/download/${fHash}">`;
                  break;
              case /application\/x-matroska/.test(mimeType):
              case /video\//.test(mimeType):
                navigationElement.innerHTML = `<video class="media" controls src="/download/${fHash}">`;
                break;
                case /text\/link/.test(mimeType):
                  navigationElement.innerHTML = "⏳";
                  Util.loadLink(fHash);
                  break;
              default:
                navigationElement.innerHTML = "🏁";
                break;
              }
              navigationElement.innerHTML += `<p class="medialink"><a href="/download/${fHash}" target="_blank">🔗↗️</a></p>`;
          } else {
            navigationElement.innerText = "🏁";
          }
          navigationElement.style.transform = '';
          navigationElement.classList.remove('-blur');
        }
    } else {
        document.querySelector('body').classList.remove('-found');
        ratingContainer.classList.add('-hidden');
        if (this.navState.capability !== undefined) {
            if (this.navState.capability.canUpload) {
                uploadFileTarget.classList.remove('-hidden');
            } else {
                uploadFileTarget.classList.add('-hidden');
            }
        }

        document.getElementById('rating-report').checked = false;
        document.getElementById('rating-down').checked = false;
        document.getElementById('rating-up').checked = false;

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
    //well, nothing to do for now
    ;
  }

  get sessionId() {
    let sessionId = document.cookie.replace(/(?:(?:^|.*;\s*)sessionId\s*\=\s*([^;]*).*$)|^.*$/, "$1");
    if (sessionId === "") {
      sessionId = Util.generateUUID();
      document.cookie = `sessionId=${sessionId};max-age=7776000`;
    }
    return sessionId;
  }

  get relativeAngle() {
    return this.orientiedAngle;
  }

  get orientiedAngle() {
    return (360 + (this.navState.angle + this.orientationCurrent - this.orientationOffset))%360;
  }

}

class Util {
  static mimeToEmoji(mimeType) {
    switch (true) {
      case /image\//.test(mimeType):
        return '🖼️';
      case /audio\//.test(mimeType):
        return '🎶';
      case /application\/x-matroska/.test(mimeType):
      case /video\//.test(mimeType):
        return '🎞️';
      default:
        return '❓';
      }
  }

  static loadLink(fHash) {
    Util.get(`/download/${fHash}`)
    .then((response) => {
      const navigationElement = document.getElementById('nav-element');
      switch (true) {
        case /https:\/\/www.youtube.com\/embed\//.test(response):
          navigationElement.innerHTML = `<iframe src="${response}" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>`;
          break;
        default:
          navigationElement.innerHTML = `<a href="${response}" target="_blank">🔗↗️</a>`;
      }
      navigationElement.innerHTML += `<p class="medialink">🆗</p>`;
    })
    .catch((error) => {
      console.log(error);
      const navigationElement = document.getElementById('nav-element');
      navigationElement.innerHTML = "🏁";
    });
  }

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

  static get(url) {
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
      xmlhttp.open("GET", url);
      xmlhttp.send();
    });
    return promise;
  }

  static upload(url, data) {
    let promise = new Promise((resolve, reject) => {
      const xmlhttp = new XMLHttpRequest();
      xmlhttp.onload = () => {
        if (xmlhttp.readyState === 4) {
          if (xmlhttp.status === 200) {
            resolve(xmlhttp.response);
          } else {
            reject(xmlhttp);
          }
        }
      };
      //xmlhttp.setRequestHeader('Content-Type','multipart/form-data');
      xmlhttp.open("POST", url);
      xmlhttp.send(data);
    });
    return promise;
  }
}

// https://css-tricks.com/the-trick-to-viewport-units-on-mobile/
function setDocHeight() {
  document.documentElement.style.setProperty('--vh', `${window.innerHeight/100}px`);
};

window.addEventListener('resize', setDocHeight);
window.addEventListener('orientationchange', setDocHeight);


document.addEventListener('DOMContentLoaded', function (_evt) {
  setDocHeight();
  const navigator = new Navigator();
});
