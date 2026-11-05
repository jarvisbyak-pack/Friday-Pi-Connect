# Friday Pi Connect

**A lightweight connectivity and Telegram communication agent for the Friday ecosystem.**

- **Repository:** `jarvisbyak-pack/Friday-Pi-Connect`
- **Current project stage:** Planning and documentation
- **Target release:** V1.0.0 (not yet implemented or released)
- **Target device:** Raspberry Pi 5
- **Initial platform:** Raspberry Pi OS / Debian 13 (Trixie)
- **Primary interface:** Telegram Bot API using outbound long polling

## Product vision

Friday Pi Connect is intended to become a modular device-connectivity component of the wider Friday home-server ecosystem. Its first release is deliberately small: help the owner discover the Raspberry Pi's current network/IP details through Telegram, and provide a two-way Telegram command interface.

## V1.0 scope — planned, not implemented

1. Automatically notify the authorized Telegram user when the Pi boots online, establishes or restores internet connectivity, changes network, or changes its local IP address.
2. Reply to `/start`, `/help`, `/status`, `/ip`, `/wifi`, and `/network`.
3. Report Wi-Fi SSID when available, connection type/interface, local IP, public IP when internet access permits, gateway when available, and event time.
4. Run automatically in the background through systemd.
5. Record network events locally while offline and report queued events after connectivity returns.
6. Restrict bot access to an explicitly configured Telegram numeric user ID.

**V1.0 is not a remote shell or remote-administration product.** It will not provide arbitrary command execution, reboot controls, service management, VNC/SSH exposure, Tailscale dependency, or a public inbound endpoint.

## Current status

This repository currently contains the product plan and engineering documentation only. No bot, network-monitoring implementation, installer, systemd unit, or tested release is claimed to exist yet. The V1.0 implementation must be completed and acceptance-tested before a stable `v1.0.0` release is tagged.

## Documentation

- [Project status note](PROJECT-NOTE.md)
- [Product and development plan](docs/PROJECT_PLAN.md)
- [V1.0 requirements and acceptance criteria](docs/V1_SCOPE.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Implementation phases](docs/IMPLEMENTATION_PHASES.md)
- [Security requirements](docs/SECURITY.md)
- [Roadmap](docs/ROADMAP.md)
- [Installation, recovery and backup expectations](docs/RECOVERY.md)
- [Changelog](CHANGELOG.md)

## Download as ZIP

GitHub automatically provides a ZIP snapshot of the current repository:
[Download Friday Pi Connect source and documentation (ZIP)](https://github.com/jarvisbyak-pack/Friday-Pi-Connect/archive/refs/heads/main.zip).

## Future direction

Later versions may add device health monitoring, controlled remote operations, and integration with Friday-Personal-Cloud APIs and other Friday services. These are roadmap ideas, not V1.0 commitments. The V1.0 core should remain lightweight and independently useful.

## Development principle

Inspect the actual Raspberry Pi environment before making changes. Preserve existing Friday services and configuration. Implement and verify one phase at a time. Never commit bot tokens, authorized user secrets, runtime logs, private network histories, or machine-specific environment files.
