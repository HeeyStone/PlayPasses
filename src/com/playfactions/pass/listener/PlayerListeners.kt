package com.playfactions.pass.listener

import com.playfactions.pass.PlayPass
import com.playfactions.pass.manager.PassManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerMoveEvent

class PlayerListeners : Listener {

    init {
        PlayPass.INSTANCE.server.pluginManager.registerEvents(this, PlayPass.INSTANCE)
    }

    @EventHandler
    fun eventKillMob(e: EntityDeathEvent) {
        val killer = e.entity.killer
        if (killer is Player) {
            val passManager = PassManager(killer.uniqueId)

            if (passManager.hasPass()) {
                passManager.addProgressInQuest("mobKiller", 1)
            }
        }
    }

    @EventHandler
    fun eventPlayerMove(e: PlayerMoveEvent) {
        val passManager = PassManager(e.player.uniqueId)

        if (passManager.hasPass()) {
            if (e.from.blockX != e.to.blockX) {
                passManager.addProgressInQuest("blockWalker", 1)
            }
        }
    }

}