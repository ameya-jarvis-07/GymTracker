# GymTracker APK Signing Setup

## Overview
This document describes the keystore setup for signing release APKs for GymTracker (IronLog).

## Keystore Details

- **Keystore File**: `app/keystore.jks`
- **Key Alias**: `gymtracker_key`
- **Store Password**: `GymTracker@2026`
- **Key Password**: `GymTracker@2026`
- **Algorithm**: RSA 2048-bit
- **Validity**: 10 years (3650 days)
- **Certificate DN**: CN=GymTracker, O=IronLog, C=US

## Build Configuration

The signing configuration is automatically applied in `app/build.gradle.kts`:

```kotlin
signingConfigs {
    create("release") {
        keyAlias = "gymtracker_key"
        keyPassword = "GymTracker@2026"
        storeFile = file("keystore.jks")
        storePassword = "GymTracker@2026"
    }
}

buildTypes {
    release {
        isMinifyEnabled = false
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
        signingConfig = signingConfigs.getByName("release")
    }
}
```

## Building a Release APK

### Command Line
```bash
./gradlew assembleRelease
```

### Output Location
```
app/build/outputs/apk/release/app-release.apk
```

### Android Studio
1. Go to **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
2. The release APK will be automatically signed using the configured keystore.

## Important Notes

- **Store the keystore securely**: The `app/keystore.jks` file should be protected and backed up.
- **Password security**: Keep the store and key passwords secure. In production, consider using environment variables or secure credential management systems.
- **Version updates**: Update `versionCode` and `versionName` in `app/build.gradle.kts` before each release build.
- **Proguard/R8**: Currently minification is disabled (`isMinifyEnabled = false`). For production, enable code obfuscation by setting `isMinifyEnabled = true`.

## Distribution

The signed APK (`app-release.apk`) can be distributed via:
- **Google Play Store**: Upload to Play Console
- **Direct download**: Share via website or cloud storage
- **Beta testing**: Use Firebase App Distribution or TestFlight

## Troubleshooting

### Build fails with "Keystore file not found"
- Ensure `app/keystore.jks` exists in the project directory.
- Verify the path in `build.gradle.kts` matches the actual location.

### Build fails with "Invalid keystore format"
- Regenerate the keystore using the keytool command in the project root.

### How to regenerate keystore (if needed)
```bash
keytool -genkeypair \
  -alias gymtracker_key \
  -keyalg RSA \
  -keysize 2048 \
  -keystore app/keystore.jks \
  -dname "CN=GymTracker, O=IronLog, C=US" \
  -storepass "GymTracker@2026" \
  -keypass "GymTracker@2026" \
  -validity 3650
```

## Verification

To verify the APK is properly signed:
```bash
jarsigner -verify -verbose app/build/outputs/apk/release/app-release.apk
```

Or use `keytool` to inspect the certificate:
```bash
keytool -printcert -jarfile app/build/outputs/apk/release/app-release.apk
```

---

**Generated**: May 25, 2026  
**Project**: GymTracker (IronLog)  
**Version**: 1.0

