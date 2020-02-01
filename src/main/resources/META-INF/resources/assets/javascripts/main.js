class Navigator {

  constructor() {
    this.orientationFix = 0;
    this.absoluteAngle = 0;
    this.geoLocationOptions = {
      enableHighAccuracy: true,
      timeout: 1000,
      maximumAge: 2000
    };
  }

  start() {
    this.heartBeatInterval = setInterval(this.heartbeat.bind(this), 2000);
    window.addEventListener("deviceorientation", this.handleOrientation.bind(this), true);
    EmojiPicker.show();
    document.querySelector('.emoji-status.-mine').addEventListener('click', EmojiPicker.showPicker);
  }

  stop() {
    clearInterval(this.heartBeatInterval);
    window.removeEventListener("deviceorientation", this.handleOrientation.bind(this));
  }

  heartbeat() {
    document.querySelector('#nav-element').classList.toggle('-heartbeat');
    window.navigator.geolocation.getCurrentPosition(this.updateCoordinates.bind(this), this.noGeoPositionAvailable.bind(this), this.geoLocationOptions);
  }

  updateCoordinates(position) {
    const latitude = position.coords.latitude;
    const longitude = position.coords.longitude;
    const url = `https://ärro.de/point/${this.sessionId}`;
    const contentType = "application/json;charset=UTF-8";
    const params = { "direction": 0, "latitude": latitude, "longitude": longitude, status: EmojiPicker.emoji };

    Util.post(url, contentType, params)
      .then((response) => {
        const div = document.getElementById('status-container');
        const direction = JSON.parse(response);
        div.classList.add('-active-position');

        this.absoluteAngle = direction.angle;
        EmojiPicker.targetStatus = direction.status;
        this.updateNavigation();
      })
      .catch((error) => console.log(error));

  }

  updateNavigation() {
    const navigationElement = document.getElementById('nav-element');
    const debugElement = document.getElementById('debug-container');

    debugElement.innerText += `absoluteAngle : ${this.absoluteAngle}\n`;
    debugElement.innerText += `orientationFix : ${this.orientationFix}\n`;
    navigationElement.style.transform = `rotate(${this.relativeAngle}deg)`;
  }

  noGeoPositionAvailable(err) {
    console.warn(`error: ${err.message}`);
    document.getElementById('status-container').classList.remove('-active-position');
  }

  handleOrientation(evt) {
    const debugElement = document.getElementById('debug-container');
    debugElement.innerText = `northed : ${evt.absolute}\n`;
    this.orientationFix = evt.alpha;
    this.updateNavigation();
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
    return this.absoluteAngle; // ToDo: Add Orientationfix after getting the north pole+ this.orientationFix;
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

class EmojiPicker{
  static get emojiCodes(){

    return this.emojis.flatMap((emoji) => emoji.code);
  }

  static get emojis(){
    return this.peopleEmojis.concat(this.otherEmojis);
  }

  static get peopleEmojis(){
    return [
      { code: "\u{1F4A5}", name: "collision" },
      { code: "\u{2620}", name: "skull and crossbones" }
    ]
  }

  static get otherEmojis(){
    return [
      { code: "\u{1F631}", name: 'face screaming in fear' },
    ]
  }

  static valid(emojiCode){
    return !!EmojiPicker.emojiCodes.find((emoji) => emoji == emojiCode);
  }

  static get emoji(){
    return localStorage.getItem('status-emoji') || this.setRandomEmoji();
  }

  static set emoji(emoji){
    localStorage.setItem('status-emoji', emoji);
    this.show();
  }

  static show(){
    document.querySelector('.emoji-status.-mine').innerText = this.emoji;
  }

  static setRandomEmoji(){
    const randomEmoji = this.emojiCodes[Math.floor(Math.random() * this.emojiCodes.length)];
    this.emoji = randomEmoji;
    return randomEmoji;
  }

  static showPicker(evt){
    const newEmojis = Array.from(prompt());
    const firstEmoji = newEmojis[0];
    if (EmojiPicker.valid(firstEmoji)){
      EmojiPicker.emoji = newEmojis[0];
    }else{
      EmojiPicker.emoji = '😢';
    }
    // ToDo: Support multiple emojis;
  }

  static set targetStatus(status){
    const targetEmojiElement = document.querySelector('.emoji-status.-target');
    if(status){
      targetEmojiElement.innerText = status;
      targetEmojiElement.classList.add('-visible');
    }else{
      targetEmojiElement.classList.remove('-visible');
    }
  }
}

document.addEventListener('DOMContentLoaded', function (_evt) {
  const navigator = new Navigator();
  navigator.start();
});
