package dev.serverreload

import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer
import org.slf4j.LoggerFactory
import java.util.Locale

/**
 * Announces every data-pack reload in chat with timing and a success or failure result.
 * The data-pack load during startup is skipped, so only reloads after boot are announced.
 */
object ServerReloadMod : DedicatedServerModInitializer {

    private val LOG = LoggerFactory.getLogger("server-reload")

    @Volatile private var started = false
    @Volatile private var reloadStartNanos = 0L

    override fun onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(ServerLifecycleEvents.ServerStarted { started = true })

        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register(
            ServerLifecycleEvents.StartDataPackReload { server, _ ->
                if (!started) return@StartDataPackReload
                reloadStartNanos = System.nanoTime()
                broadcast(server, "&8[&6&l*&8] &eReloading &7server data...")
            },
        )

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(
            ServerLifecycleEvents.EndDataPackReload { server, _, success ->
                if (!started) return@EndDataPackReload
                val secs = (System.nanoTime() - reloadStartNanos) / 1_000_000 / 1000.0
                val time = String.format(Locale.ROOT, "%.2f", secs)
                if (success) {
                    broadcast(server, "&8[&a&l+&8] &aReload complete &7in &f${time}s&7.")
                    LOG.info("Data-pack reload complete in {}s.", time)
                } else {
                    broadcast(server, "&8[&c&lx&8] &cReload failed&7. Check console; previous data kept.")
                    LOG.warn("Data-pack reload FAILED after {}s, previous data kept.", time)
                }
            },
        )

        LOG.info("Server Reload announcer ready.")
    }

    private fun broadcast(server: MinecraftServer, legacy: String) {
        val msg = Fmt.legacy(legacy)
        server.playerList.players.forEach { it.sendSystemMessage(msg) }
    }
}
