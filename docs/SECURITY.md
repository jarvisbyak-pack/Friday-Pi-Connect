# Security Requirements

## V1.0 security baseline

1. **Telegram sender allowlist:** Authorize exact numeric Telegram user IDs. Do not rely only on a username or display name.
2. **Secret storage:** Store the bot token in a root-readable or dedicated-service-readable environment/config file outside the repository. Never commit tokens, `.env` files, or secret screenshots.
3. **Least privilege:** Run under a dedicated unprivileged service account where practical. Grant only the read access needed for network diagnostics.
4. **Read-only command set:** V1.0 supports information queries only. No shell command, arbitrary Python, file browsing, reboot, shutdown, or service control through Telegram.
5. **No public listener:** Use outbound HTTPS Telegram long polling. Do not configure public webhooks, port forwarding, or an internet-facing management endpoint.
6. **Input handling:** Treat all Telegram text as untrusted. Use fixed command routing and strict parsing; never concatenate message text into shell commands.
7. **Data minimization:** Store only the minimal event metadata needed for duplicate suppression and outage recovery. Avoid retaining unnecessary network history.
8. **Logging:** Do not log bot tokens or full secret configuration. Log authorization failures without exposing sensitive device details.
9. **Network lookup:** Validate public IP responses and use HTTPS with timeouts. A failed external lookup must not break local diagnostics.
10. **Updates:** Document ownership and permissions for code, config, logs, and state database.

## Threats to test

- Unauthorized Telegram user sends valid commands.
- Bot token is accidentally included in Git changes.
- Malformed or oversized Telegram message.
- Telegram API is unreachable or returns errors.
- Public IP lookup service is unavailable or returns invalid content.
- Network flaps cause notification storms.
- Local event database is corrupt or unavailable.

## Explicit V1.0 limitation

Telegram control is not a substitute for a hardened remote-administration channel. Do not add remote shell or privileged administrative operations to V1.0.
