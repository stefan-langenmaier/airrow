class Navigator {

  constructor() {
    this.absoluteAngle = 0;
    this.activeAngle = 0;
    
    this.orientationAbsolute = false;
    this.orientationOffset = null;
    this.orientationCurrent = 0;
    
    this.geoLocationOptions = {
      enableHighAccuracy: true,
      timeout: 1000
    };
  }

  refreshOffset(summary) {
    const oldOrientationOffset = this.orientationOffset;
    this.orientationOffset = summary.northOffset;
    if (oldOrientationOffset === null) { this.updateSetup(); }
  }

  updateSetup() {
//    if (this.orientationAbsolute === false) {
//      this.orientationOffset = this.orientationCurrent;
//    }
    this.hideSetup();
    this.showNavigation();
  }

  hideSetup() {
    document.querySelector('.setup-container').classList.add('-hidden');
  }

  showNavigation() {
    document.querySelector('.nav-container').classList.remove('-hidden');
    this.start();
  }

  handleOrientation(evt) {
    this.orientationAbsolute = evt.absolute;
    this.orientationCurrent = evt.alpha;
  }

  updateDebug() {
    const debug = document.getElementById('debug-container');
    debug.innerText = `northed: ${this.orientiedAngle}`;
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
    const url = `https://ärro.de/point/${this.sessionId}`;
    const contentType = "application/json;charset=UTF-8";
    const params = { "direction": 0, "latitude": latitude, "longitude": longitude };

    Util.post(url, contentType, params)
      .then((response) => {
        const div = document.getElementById('status-container');
        const direction = JSON.parse(response);
        div.classList.add('-active-position');

        this.absoluteAngle = direction.angle;
        this.updateNavigation();
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
    // rotate in the direction of the smaller angle
    // this avoids jumping between 0 and 359 angles
    let angleChange = Math.abs(this.activeAngle - this.orientiedAngle);
    if (angleChange > 180) { angleChange = (-1)*(360-angleChange);}
    this.activeAngle += angleChange;

    // if it slowly rotates out dial it back
    if (Math.abs(this.activeAngle) > 360) {this.activeAngle %= 360;}

    return this.activeAngle;
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
  const compass = new Compass();
  compass.start();
  compass.register(navigator.refreshOffset.bind(navigator));
//  navigator.setup();
//  navigator.hideSetup();
//  navigator.showNavigation();
});
