# Architecture — Friday Pi Connect V1.0

## 1. High-level design

```text
Wi-Fi / Ethernet
       |
       v
OS network event source + periodic connectivity verification
       |
       v
Network state collector
       |-- interface and connection type
       |-- SSID / signal where available
       |-- local IP and gateway
       |-- public IP lookup when online
       |-- internet reachability state
       |
       v
State change detector + local event queue
       |
       +--------------------+
       |                    |
       v                    v
Telegram notifier     Telegram long-polling bot
       |                    |
       +--------- HTTPS ----+
                 |
                 v
             Telegram
                 |
                 v
          Owner's phone
```

## 2. Components

- **Network event adapter:** Uses the network management mechanism discovered during the target-device audit. NetworkManager dispatcher may be used if NetworkManager is active; do not assume it is installed or authoritative.
- **Connectivity verifier:** Confirms usable internet access rather than treating Wi-Fi association as internet availability. Use bounded timeouts, multiple checks, and backoff to reduce false positives.
- **Network collector:** Reads local interface/address/route data using standard OS interfaces and tools. Collect SSID only when relevant and available.
- **Public IP resolver:** Uses a configurable HTTPS endpoint with timeout and validation. Public IP is optional; failure must not block local IP reporting or bot responses.
- **State/event manager:** Compares current state with last known state, deduplicates events, and persists outage/change timestamps locally.
- **Telegram adapter:** Uses Bot API long polling over outbound HTTPS. No webhook listener or inbound port is required.
- **Authorization layer:** Allows only configured numeric Telegram user IDs.
- **Service wrapper:** Runs the agent through systemd with restart policy and bounded logs.

## 3. Initial command flow

`Telegram update -> sender ID validation -> command router -> read-only network collector -> response formatter -> Telegram reply`.

The command router must not invoke a shell with user-supplied text. V1.0 commands are read-only.

## 4. Suggested internal module boundaries

- `config`: environment/config loading and validation.
- `network`: event adapters, reachability checks, and network information collection.
- `state`: state comparison, deduplication, and local event persistence.
- `telegram`: polling, authorization, command handlers, and notifications.
- `service`: lifecycle, graceful shutdown, and logging.

## 5. Reliability behavior

- Network monitoring must continue when Telegram is unreachable.
- Telegram polling should reconnect with bounded exponential backoff.
- Public IP lookup must be optional and time-bounded.
- Persist only the minimum operational event history needed for offline recovery.
- On restart, reconcile current state with persisted state without replaying an unlimited backlog.
- Use UTC timestamps internally and render local time consistently for the user.

## 6. Deployment choice

Prefer one systemd-managed Python process for V1.0 to keep deployment and recovery simple. Separate services can be considered only if implementation/testing demonstrates a concrete reliability benefit.
