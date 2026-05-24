# GymTracker Deployment Guide

## Release APK

The signed release APK is ready for distribution.

### APK Information
- **File**: `app/build/outputs/apk/release/app-release.apk`
- **Size**: ~9 MB
- **Package Name**: `com.jarvis.gymtracker`
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 35 (Android 15)
- **Signature**: Signed with production keystore (valid for 10 years)

## Distribution Options

### 1. Google Play Store
1. Create a Google Play Console account
2. Create a new app entry
3. Upload the APK via the console
4. Fill in store listing details (description, screenshots, etc.)
5. Set pricing and availability
6. Submit for review

### 2. Firebase App Distribution
```bash
./gradlew appDistributionUploadRelease
```
Requires Firebase configuration and credentials.

### 3. Direct Download
- Host `app-release.apk` on your server or cloud storage
- Provide download link to users
- Users can install via "Install from Unknown Sources" (after enabling in settings)

### 4. GitHub Releases
```bash
gh release create v1.0 app/build/outputs/apk/release/app-release.apk --title "GymTracker v1.0"
```

## Pre-Release Checklist

Before distributing, verify:
- [ ] Version code incremented in `build.gradle.kts`
- [ ] Version name updated (current: 1.0)
- [ ] All app permissions documented
- [ ] Privacy policy available
- [ ] Terms of service available (if applicable)
- [ ] App tested on multiple Android versions (8.0 - 15)
- [ ] APK size acceptable (~9 MB is reasonable)
- [ ] All features tested and working
- [ ] No debug/test code present
- [ ] ProGuard/R8 minification enabled (optional but recommended for production)

## Version Management

To prepare for the next release:

1. **Update version code** (for app store tracking):
   ```kotlin
   versionCode = 2  // Increment by 1
   ```

2. **Update version name** (user-facing):
   ```kotlin
   versionName = "1.1"
   ```

3. **Update release notes** for store listing

4. **Rebuild release APK**:
   ```bash
   ./gradlew assembleRelease
   ```

## Testing Signed APK Locally

To install and test the signed APK on a device:

```bash
adb install -r app/build/outputs/apk/release/app-release.apk
```

Or drag and drop into Android Studio's emulator.

## Security Notes

- **Keystore Protection**: Keep `app/keystore.jks` in a secure location, backed up regularly
- **Password Management**: Store keystore passwords securely; never commit to version control
- **Signature Verification**: Your release APK signature must remain consistent across versions for app store updates

## Additional Resources

- [Google Play Security & Compliance](https://play.google.com/about/developer-content-policy/)
- [Android Developer Guide](https://developer.android.com/distribute)
- [Firebase App Distribution](https://firebase.google.com/docs/app-distribution)

---

**Project**: GymTracker (IronLog)  
**Version**: 1.0  
**Last Updated**: May 25, 2026

