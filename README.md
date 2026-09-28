# Server Reload

Announces data-pack reloads in chat, with timing and whether they worked.

`/reload` is silent to everyone who didn't type it, and it fails quietly - the server keeps the old
data and says so only in the console. This puts both ends in chat:

```
[*] Reloading server data...
[+] Reload complete in 1.24s.
```

or, when it goes wrong:

```
[x] Reload failed. Check console; previous data kept.
```

The load that happens during startup is skipped, so only reloads after boot are announced.

## Server-side only - players install nothing

Vanilla clients render the messages fine. Nothing to install on a client.

## Requirements

Drop these into your **server's** `mods/` folder:

| Mod | Version |
| --- | --- |
| `server-reload-0.1.0.jar` | this mod |
| [Fabric API](https://modrinth.com/mod/fabric-api) | `0.155.2+26.2` (or compatible) |
| [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) | `1.13.12+kotlin.2.4.0` (or compatible) |

Minecraft **26.2**, Fabric Loader **0.19.3+**, **Java 25**.

## Install

Put the three jars in `<server>/mods/` and start the server. There is no config and no command.

## Limits

The messages are hardcoded, including their colours. Timing is wall-clock around the reload
lifecycle, so it includes anything else hooked into the same events, not just vanilla's work.
