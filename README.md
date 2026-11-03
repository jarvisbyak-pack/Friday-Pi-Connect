# Friday TV Launcher

A lightweight, remote-first Android TV home launcher for the Realme TechLife RMI41F (Android 14). The current implementation uses native Android Java views only; it has no Compose runtime, third-party UI toolkit, network service, or background polling service.

## Version 0.2.0 — implementation stage 1

- Native HOME / Leanback launcher entry point; installs alongside Google TV.
- Full-screen dark cinematic gradient background.
- Horizontal, D-pad navigable app dock with installed Leanback apps and launchable-app fallback.
- Preferred app ordering for common streaming and home-security apps.
- Focus scaling, high-contrast focus outline, and remote-friendly touch targets.
- Right-side Control Center with local clock/date and shortcuts to Wi-Fi, Bluetooth, Display, and Android Settings.
- Clock refreshes every 30 seconds while the launcher is visible; no continuous animation loop.
- No real-time blur, video wallpaper, analytics, internet access, or persistent service.

## Target hardware

- Realme TechLife TV model RMI41F / platform Realtek RTD2841A.
- Android 14 (API 34), ARMv7 userspace, 1920×1080 physical panel.
- Approximately 940 MB reported system RAM. Keep artwork and UI effects lightweight.

## Build

Requires JDK 17+, Gradle 8.9+, Android SDK platform 35, and Android Build Tools 35.0.0.

```bash
gradle --no-daemon assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## Safety

The stock Google TV launcher is not disabled, uninstalled, or modified. This project does not set itself as the default HOME app, change TV settings, or remove packages. Install and test the APK as a secondary launcher first. Only change the default HOME app after explicit approval and successful remote-navigation testing.

## Next implementation stages

1. Validate rendering, focus navigation, app discovery, and launch behaviour on the TV.
2. Add a real media-session card and verified device-status indicators.
3. Add favourites/reordering and a lightweight wallpaper picker.
4. Add profile selection and a screensaver only after measuring memory/CPU impact.
