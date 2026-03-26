# RickrollStickers - Build Instructions

## Prerequisites (on your Kali machine)
```bash
# Install Android SDK command-line tools
sudo apt install android-sdk    # or download from developer.android.com/studio

# Set ANDROID_HOME
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

# Accept licenses
yes | sdkmanager --licenses
sdkmanager "platforms;android-34" "build-tools;34.0.0"
```

## Build
```bash
cd RickrollStickers

# Download gradle wrapper
gradle wrapper --gradle-version 8.2

# Build debug APK (no signing needed for sideload)
./gradlew assembleDebug
```

APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

## Install on phone
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```
Or just transfer the APK to phone and tap to install (enable "Install unknown apps" in settings).

## How it works
1. Open the app → tap "Add to WhatsApp"
2. Pack gets added to WhatsApp
3. When anyone taps a sticker → "View sticker pack" → rickroll 😈

## The magic line (StickerContentProvider.java)
```java
private static final String PUBLISHER_URL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
```
WhatsApp reads `sticker_pack_publisher_website` from the ContentProvider and uses it
as the redirect for the "View sticker pack" button. No third-party app involved.
