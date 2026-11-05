# Friday Pi Connect — Product & Development Plan

**Document version:** 1.0  
**Planning baseline:** 2026-11-05  
**Product status:** Planned; implementation not started  
**Target release:** V1.0.0

## 1. Purpose

Friday Pi Connect is a lightweight Raspberry Pi agent that provides reliable network/IP visibility through Telegram. It is intended to become one independently deployable component of the broader Friday home-server ecosystem.

The immediate user problem is straightforward: the Raspberry Pi may be carried between locations and used without a screen. When it joins a network, the owner needs its current address and a way to ask the device for its current connection details through Telegram.

## 2. V1.0 outcomes

- Deliver a Telegram notification after boot when internet connectivity becomes available.
- Detect internet loss and restoration, network changes, and local IP changes.
- Report the current local IP and public IP (when internet access allows), Wi-Fi SSID when applicable, connection type, interface, gateway, and timestamp.
- Provide a small, authorized two-way Telegram command interface.
- Start automatically after reboot and recover from process failure.
- Preserve offline network events locally and send a concise recovery summary when Telegram connectivity returns.

## 3. Explicit non-goals for V1.0

- General-purpose remote shell or arbitrary command execution.
- Remote reboot, shutdown, or service administration.
- VNC, SSH, or web-based remote desktop access.
- Tailscale or another remote-access overlay requirement.
- Public inbound webhooks, port forwarding, or exposing a listener to the internet.
- AI assistant, LLM integration, dashboard, fleet management, or broad device orchestration.

## 4. Primary user journeys

### Journey A — automatic connection report

1. Raspberry Pi boots or connects to a network.
2. The agent waits until actual internet connectivity is verified; Wi-Fi association alone is insufficient.
3. It gathers available network details.
4. It sends a formatted Telegram message to the configured authorized user.
5. It suppresses duplicate notifications for the same unchanged state.

### Journey B — ask for current IP

1. Owner sends `/ip` to the bot.
2. Bot verifies the sender's numeric Telegram user ID.
3. Bot gathers fresh network details.
4. Bot replies with local IP, public IP if reachable, interface, SSID if applicable, gateway if available, and connectivity status.

### Journey C — internet outage and recovery

1. Agent detects loss of internet while the device may remain associated with Wi-Fi.
2. It records the outage and time locally; it cannot deliver a Telegram message while offline.
3. When internet returns, it reports recovery and includes the recorded outage/recovery times.

## 5. Operating principles

- Small, modular, observable, and easy to recover.
- Outbound Telegram polling only; no inbound network listener required.
- Use the network manager actually present on the target Pi; inspect before selecting NetworkManager dispatcher or another event source.
- Use independent internet reachability checks with timeouts and conservative retry/backoff.
- Keep Telegram delivery failure separate from network detection failure.
- Avoid unnecessary dependencies and privileged execution.
- Document every change and verify it before moving to the next phase.

## 6. Relationship to Friday-Personal-Cloud

V1.0 runs independently. Its internal modules and status output should be designed so a future, documented integration adapter can expose device/network status to Friday-Personal-Cloud. Do not add a hard dependency on the home-server API or MCP in V1.0.

## 7. Project governance

- `main` should contain reviewed, stable documentation and code.
- Use descriptive commits and maintain `CHANGELOG.md`.
- Keep `VERSION` aligned with the current development/documentation baseline.
- Tag `v1.0.0` only after all V1.0 acceptance criteria pass on the target Raspberry Pi.
- Keep future features in `ROADMAP.md`; do not imply they are implemented.
