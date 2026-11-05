# V1.0 Scope and Acceptance Criteria

**Release label:** V1.0 — Connectivity & Telegram Foundation  
**Status:** Planned / not implemented

## 1. Included features

### Automatic network notifications

- Detect boot-time connectivity.
- Detect internet connected, disconnected, and restored states.
- Detect Wi-Fi SSID changes when available.
- Detect interface and local IP changes.
- Support Wi-Fi and Ethernet where supported by the OS network stack.
- Report local IPv4, public IPv4 when reachable, gateway, interface, connection type, SSID when applicable, and timestamp.
- Avoid repeated alerts for unchanged state.
- Queue outage/change events locally when Telegram is unreachable and deliver a bounded summary after recovery.

### Telegram commands

| Command | Required response |
|---|---|
| `/start` | Welcome, device identity, and short usage guidance |
| `/help` | Supported V1.0 commands |
| `/status` | Current internet and interface status plus key IP details |
| `/ip` | Fresh local and public IP details |
| `/wifi` | SSID, interface, signal strength when supported, or a clear not-applicable response |
| `/network` | Expanded current network details |

Unknown commands should receive a safe help response. Unauthorized users should not receive device information or trigger actions.

### Deployment

- One documented systemd service is preferred for the initial lightweight agent, unless audit findings justify a different design.
- Automatic start at boot and restart on unexpected process exit.
- Least-privilege service account where practical.
- Journald or controlled application logs with rotation/size limits.
- Installation, update, uninstall, and recovery instructions.

## 2. Not included

Remote shell, arbitrary command execution, reboot/shutdown, service restart controls, VNC/SSH management, Tailscale, dashboard, AI, MCP, and broad Friday platform administration.

## 3. Acceptance tests

| ID | Scenario | Pass condition |
|---|---|---|
| AT-01 | Cold boot with internet | One accurate online notification is delivered |
| AT-02 | Wi-Fi connected but no internet | No false online notification is sent |
| AT-03 | Internet becomes available | Connection notification includes current details |
| AT-04 | Wi-Fi network changes | New SSID and IP state are reported |
| AT-05 | DHCP changes local IP | Changed local IP is detected and reported |
| AT-06 | Ethernet connection | Interface and IP are reported accurately |
| AT-07 | Internet loss | Event is recorded locally without claiming Telegram delivery |
| AT-08 | Internet restoration | Recovery notification includes outage/recovery timing |
| AT-09 | Telegram temporary outage | Agent continues monitoring and retries safely |
| AT-10 | `/ip` | Authorized user receives fresh local/public IP results |
| AT-11 | `/status` | Authorized user receives accurate connectivity status |
| AT-12 | `/wifi` | SSID/signal are returned when supported; otherwise clearly unavailable |
| AT-13 | Unauthorized Telegram sender | No device information is disclosed and no operation is run |
| AT-14 | Reboot | Service starts automatically and resumes monitoring |
| AT-15 | Duplicate network events | No notification storm occurs |
| AT-16 | Public IP lookup unavailable | Bot remains responsive and clearly marks public IP unavailable |
| AT-17 | Long-running test | No unexpected crash or uncontrolled log growth |

## 4. Release gate

Do not call V1.0 complete, publish a stable release, or create a `v1.0.0` tag until all critical acceptance tests pass on the actual target Raspberry Pi and the test evidence is recorded.
