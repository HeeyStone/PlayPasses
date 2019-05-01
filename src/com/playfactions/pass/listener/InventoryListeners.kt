package com.playfactions.pass.listener

import com.playfactions.kotlin.util.other.ActionbarUtil
import com.playfactions.kotlin.util.sendActionBarMessage
import com.playfactions.kotlin.util.sendTitle
import com.playfactions.pass.PlayPass
import com.playfactions.pass.manager.PassManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryListeners : Listener {

    init {
        PlayPass.INSTANCE.server.pluginManager.registerEvents(this, PlayPass.INSTANCE)
    }

    @EventHandler
    fun eventInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        if (e.clickedInventory.title == "Missões do passe de elite") {
            e.isCancelled = true
            val passManager = PassManager(p.uniqueId)

            when (e.rawSlot) {
                10 ->
                    if (!passManager.isCollectedQuest("mobKiller") && passManager.getQuestProgress("mobKiller")!! >= 1000) {
                        p.closeInventory()
                        Bukkit.getOnlinePlayers().forEach {
                            if (it.name != p.name) {
                                it.sendActionBarMessage("§aO jogador §f${p.name}§a concluiu a missão §f#1§a do passe de elite" )
                            }
                        }
                        p.sendTitle("§a§lMissões", "§fVocê concluiu uma missão e recebeu seu premio!", 0, 20*3, 0)
                        ActionbarUtil(p).sendActionBarMessage("§aVocê concluiu a missão §f#1§a do passe de elite!", 5, PlayPass.INSTANCE)

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cash setar ${p.name} 20000")
                        passManager.colectQuest("mobKiller")
                    }
            }
        }
    }

}