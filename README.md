# Friday TV Launcher

A lightweight Android TV home launcher with a premium, iOS-inspired interface, designed for the Realme TechLife RMI41F (Android 14).

## Safety-first installation

The launcher is installed alongside the stock Google TV launcher. Keep the stock launcher enabled as a fallback. Do not disable or remove system packages. Test app launching, D-pad navigation, voice search, HDMI/CEC, casting, Bluetooth remote and USB media before selecting Friday TV as the default HOME app.

## Build

Use the included GitHub Actions workflow to build the debug APK. The workflow uses a hosted x86_64 runner because Google's Android SDK build tools do not currently provide a complete Linux ARM64 toolchain.

## Included app shortcuts

Netflix, YouTube, Prime Video, Spotify, Disney+, EZVIZ and UniFi Protect are prioritised when installed. Other launchable TV apps are discovered automatically.
