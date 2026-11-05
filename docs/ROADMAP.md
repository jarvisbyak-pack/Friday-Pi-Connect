# Product Roadmap

The roadmap describes possible future directions. These features are **not implemented** and are not part of the V1.0 acceptance criteria.

## V1.0 — Connectivity & Telegram Foundation (current target)

- Automatic network/IP notifications.
- Two-way Telegram bot communication.
- Read-only commands: `/start`, `/help`, `/status`, `/ip`, `/wifi`, `/network`.
- Automatic systemd startup and basic recovery.

## V1.x — Reliability improvements (after V1.0)

- Additional network diagnostics and configurable notification preferences.
- Improved event history and health reporting.
- Additional platform compatibility where justified by testing.

## V2.0 — Device monitoring (future)

- CPU temperature and utilization.
- RAM and storage capacity.
- Uptime and system health summaries.
- Selected Friday service health checks.

## V3.0 — Controlled remote operations (future; separate security review)

- Carefully allowlisted administrative actions.
- Explicit confirmation and short-lived authorization for sensitive actions.
- Audit records and operational safeguards.
- No unrestricted remote shell.

## V4.0 — Friday ecosystem integration (future)

- Documented integration adapter for Friday-Personal-Cloud.
- Device registration and identity.
- Status exchange with Friday REST API where appropriate.
- MCP tools for selected, permission-controlled device information/actions.
- Potential centralized device management across the Friday ecosystem.

## Roadmap governance

Future features must be scoped, threat-modelled, versioned, and tested before implementation. Do not advertise roadmap items as existing capabilities.
