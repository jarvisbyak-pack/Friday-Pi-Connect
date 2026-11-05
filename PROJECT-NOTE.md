# Project Note — Friday Pi Connect V1.0

**Repository baseline date:** 2026-11-05  
**Project state:** PLANNING / DOCUMENTATION ONLY  
**V1.0 implementation:** NOT STARTED  
**Stable V1.0 release:** NOT PUBLISHED

## What this repository is for

Friday Pi Connect is a planned, lightweight Raspberry Pi 5 agent that solves one immediate problem: obtain the Pi's current network and IP information through Telegram, even when the device is running headless.

## What V1.0 is intended to implement

- Two-way Telegram bot communication.
- Automatic Telegram notification when internet connectivity becomes available.
- Detection of internet loss/restoration, Wi-Fi network changes, and local IP changes.
- Read-only commands: `/start`, `/help`, `/status`, `/ip`, `/wifi`, and `/network`.
- Local IP and public IP reporting, with SSID, interface, connection type, and gateway when available.
- Automatic startup through systemd.
- Telegram sender authorization using the owner's numeric Telegram user ID.

## What V1.0 intentionally does not include

No remote shell, reboot controls, service administration, VNC/SSH exposure, Tailscale requirement, public inbound endpoint, dashboard, AI, or MCP integration.

## Future direction

The project is expected to grow in later versions and may eventually integrate with Friday-Personal-Cloud as a device connectivity/management component. Future ideas are documented in `docs/ROADMAP.md` and must not be described as already implemented.

## Development status rule

This repository currently records the approved product direction and engineering plan. Implementation, installation, and acceptance testing are still pending. Do not tag or announce V1.0 as complete until the acceptance matrix in `docs/V1_SCOPE.md` passes on the target Raspberry Pi.

See `README.md` and the `docs/` directory for the full project plan, architecture, phases, security requirements, recovery expectations, and roadmap.
