# PWA Builder

https://github.com/pwa-builder/CloudAPK/blob/master/Next-steps-unsigned.md

# Setup signing

$ keytool -genkey -v -keystore airrow.keystore -alias airrow -keyalg RSA -keysize 2048 -validity 10000


$ /opt/android-sdk-update-manager/build-tools/29.0.3/apksigner sign -ks airrow.keystore --ks-key-alias airrow Ärro-unsigned.apk 
$ /opt/android-sdk-update-manager/build-tools/29.0.3/apksigner verify Ärro-unsigned.apk 

$ /opt/android-sdk-update-manager/build-tools/29.0.3/zipalign -p 4 Ärro-unsigned.apk  airrow.apk
$ /opt/android-sdk-update-manager/build-tools/29.0.3/zipalign -c 4 airrow.apk
