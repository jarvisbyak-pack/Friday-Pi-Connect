# Installation, Backup, and Recovery Requirements

This document defines what the implementation must provide. It is not an installation guide for a service that has not yet been built.

## Before installation

- Confirm the target Raspberry Pi and OS version.
- Back up any files that may be changed.
- Record active network manager, interfaces, existing systemd units, and Friday service status.
- Confirm the actual approved project working directory and storage device.
- Do not repartition, format, or repurpose existing storage.

## Required installation documentation

The eventual installer guide must describe prerequisites, Python environment creation, dependency installation, service account setup, token/user-ID configuration, file permissions, systemd installation, verification, and uninstall/rollback.

## Backup requirements

Back up only what is necessary:

- Version-controlled source and documentation (GitHub is the source-code recovery point).
- A separately protected copy of runtime configuration, excluding unnecessary secrets from ordinary backups.
- Minimal local event state if preserving outage history is useful.

Bot tokens must be handled as secrets and must not be placed in a public archive or Git repository.

## Recovery procedure requirements

1. Restore the source from the verified GitHub repository/tag.
2. Recreate the Python environment using documented pinned dependencies.
3. Restore or re-enter the bot token and authorized Telegram user ID securely.
4. Restore the systemd unit and file permissions.
5. Start the service and inspect its logs.
6. Send `/status` and `/ip` from the authorized Telegram account.
7. Trigger a controlled network reconnect and verify one correct notification.

## Rollback requirements

- Provide an uninstall command/procedure that stops and disables only the Friday Pi Connect service.
- Remove only files created by this project after confirming paths.
- Preserve existing Friday services, network configuration, user data, and unrelated systemd units.
- Never overwrite or delete existing configuration without explicit review and backup.
