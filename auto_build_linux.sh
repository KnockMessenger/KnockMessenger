./gradlew test
./gradlew lint
./gradlew assembleDebug
scp -o "StrictHostKeyChecking no" -i key_lin.ppk app/build/outputs/apk/debug/app-debug.apk c4367knockMessenger@wh04.rackhost.hu:../../web/vadaszfoto.hu/KnockMessenger