# Implementation Phases

**Execution rule:** Complete, verify, and document each phase before proceeding. Do not install packages or alter services before Phase 0 inspection.

## Phase 0 — Target device audit

- Confirm Raspberry Pi model, OS release, kernel, Python version, storage, and current user.
- Identify active network manager and interfaces (Wi-Fi/Ethernet).
- Inspect current routes, DNS, and internet behavior.
- Inspect existing Friday services, systemd units, repository directories, and port usage.
- Confirm project working location and backup destination.
- Record baseline and rollback plan.

**Exit gate:** Written audit report; no unapproved changes made.

## Phase 1 — Telegram foundation

- Create bot through BotFather (owner performs token creation).
- Configure token and authorized numeric Telegram user ID outside Git.
- Implement long polling, sender authorization, `/start`, and `/help`.
- Verify authorized and unauthorized sender behavior.

**Exit gate:** Two-way Telegram communication works securely; no network monitor yet.

## Phase 2 — Network collector and commands

- Implement current network collection.
- Implement internet reachability verification.
- Implement `/status`, `/ip`, `/wifi`, and `/network`.
- Clearly handle unavailable SSID, gateway, or public IP.

**Exit gate:** Command output matches live Raspberry Pi state across Wi-Fi and Ethernet tests where available.

## Phase 3 — Automatic event detection and notifications

- Integrate with the network event source identified in Phase 0.
- Add periodic reconciliation to catch missed events.
- Detect boot-online, internet loss/restoration, network change, and local IP change.
- Add deduplication and local event persistence.
- Add safe retry and recovery summaries.

**Exit gate:** Event scenarios pass without false-online or duplicate notification storms.

## Phase 4 — systemd deployment and hardening

- Add a least-privilege service account if feasible.
- Add systemd unit, restart behavior, startup configuration, and bounded logging.
- Verify token/config file permissions.
- Document install, update, uninstall, and rollback.

**Exit gate:** Service survives reboot and controlled network/Telegram interruptions.

## Phase 5 — Verification and V1.0 release preparation

- Run the full acceptance matrix in `V1_SCOPE.md`.
- Run a stability test and inspect logs/storage growth.
- Review secret handling and unauthorized access tests.
- Update README, CHANGELOG, VERSION, and test evidence.
- Prepare release notes; tag `v1.0.0` only after all release gates pass.

**Exit gate:** Evidence-backed verification report and owner approval for stable release.
