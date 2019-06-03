package com.playfactions.pass.listener

import com.playfactions.pass.PlayPass
import com.playfactions.pass.manager.PassManager
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListeners : Listener {

    init {
        PlayPass.INSTANCE.server.pluginManager.registerEvents(this, PlayPass.INSTANCE)
    }

    @EventHandler
    fun eventPlayerJoin(e: PlayerJoinEvent) {
        val player = PassManager.getPlayerSQLWhereUUID(e.player.uniqueId)
        PlayPass.CACHE.put(player!!.uuid, player)
    }

    @EventHandler
    fun eventPlayerQuit(e: PlayerQuitEvent) {
        val passManager = PassManager(e.player.uniqueId)
        if (passManager.isInSQL()) {
            passManager.save()
        } else passManager.insert()
    }

    @EventHandler
    fun eventKillMob(e: EntityDeathEvent) {
        val killer = e.entity.killer
        if (killer is Player) {
            val passManager = PassManager(killer.uniqueId)

            if (passManager.hasPass()) {
                if (e.entity is Player) {
                    passManager.addProgressInQuest("playerKiller", 1)
                }  else passManager.addProgressInQuest("mobKiller", 1)
            }
        }
    }

    @EventHandler
    fun eventPlayerMove(e: PlayerMoveEvent) {
        val passManager = PassManager(e.player.uniqueId)

        if (passManager.hasPass()) {
            if (e.from.block.location.x != e.to.block.location.x) {
                passManager.addProgressInQuest("blockWalker", 1)
            }
        }
    }

    @EventHandler
    fun eventBlockBreak(e: BlockBreakEvent) {
        val passManager = PassManager(e.player.uniqueId)

        if (passManager.hasPass()) {
            passManager.addProgressInQuest("blockBreaker", 1)
        }
    }

}