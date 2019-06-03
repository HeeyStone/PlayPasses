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
        if (e.clickedInventory == null) return
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
                        p.sendTitle("§a§lMissões (§f#1§a)", "§fVocê concluiu uma missão e recebeu seu premio!", 0, 20*3, 0)
                        ActionbarUtil(p).sendActionBarMessage("§aVocê concluiu a missão §f#1§a do passe de elite!", 5, PlayPass.INSTANCE)

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cash setar ${p.name} 20000")
                        passManager.colectQuest("mobKiller")
                        passManager.addProgressInQuest("questsCompleter", 1)
                    }
                11 ->
                    if (!passManager.isCollectedQuest("blockWalker") && passManager.getQuestProgress("blockWalker")!! >= 10000) {
                        p.closeInventory()
                        Bukkit.getOnlinePlayers().forEach {
                            if (it.name != p.name) {
                                it.sendActionBarMessage("§aO jogador §f${p.name}§a concluiu a missão §f#2§a do passe de elite" )
                            }
                        }
                        p.sendTitle("§a§lMissões (§f#2§a)", "§fVocê concluiu uma missão e recebeu seu premio!", 0, 20*3, 0)
                        ActionbarUtil(p).sendActionBarMessage("§aVocê concluiu a missão §f#2§a do passe de elite!", 5, PlayPass.INSTANCE)

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cash setar ${p.name} 20000")
                        passManager.colectQuest("blockWalker")
                        passManager.addProgressInQuest("questsCompleter", 1)
                    }
                12 ->
                    if (!passManager.isCollectedQuest("blockBreaker") && passManager.getQuestProgress("blockBreaker")!! >= 30000) {
                        p.closeInventory()
                        Bukkit.getOnlinePlayers().forEach {
                            if (it.name != p.name) {
                                it.sendActionBarMessage("§aO jogador §f${p.name}§a concluiu a missão §f#3§a do passe de elite" )
                            }
                        }
                        p.sendTitle("§a§lMissões (§f#3§a)", "§fVocê concluiu uma missão e recebeu seu premio!", 0, 20*3, 0)
                        ActionbarUtil(p).sendActionBarMessage("§aVocê concluiu a missão §f#3§a do passe de elite!", 5, PlayPass.INSTANCE)

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cash setar ${p.name} 20000")
                        passManager.colectQuest("blockBreaker")
                        passManager.addProgressInQuest("questsCompleter", 1)
                    }
                13 ->
                    if (!passManager.isCollectedQuest("playerKiller") && passManager.getQuestProgress("playerKiller")!! >= 50) {
                        p.closeInventory()
                        Bukkit.getOnlinePlayers().forEach {
                            if (it.name != p.name) {
                                it.sendActionBarMessage("§aO jogador §f${p.name}§a concluiu a missão §f#2§a do passe de elite" )
                            }
                        }
                        p.sendTitle("§a§lMissões (§f#4§a)", "§fVocê concluiu uma missão e recebeu seu premio!", 0, 20*3, 0)
                        ActionbarUtil(p).sendActionBarMessage("§aVocê concluiu a missão §f#4§a do passe de elite!", 5, PlayPass.INSTANCE)

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cash setar ${p.name} 20000")
                        passManager.colectQuest("playerKiller")
                        passManager.addProgressInQuest("questsCompleter", 1)
                    }
                15 ->
                    if (!passManager.isCollectedQuest("questsCompleter") && passManager.getQuestProgress("questsCompleter")!! >= 4) {
                        p.closeInventory()
                        Bukkit.getOnlinePlayers().forEach {
                            if (it.name != p.name) {
                                it.sendActionBarMessage("§aO jogador §f${p.name}§a concluiu a missão §f#5§a do passe de elite" )
                            }
                        }
                        p.sendTitle("§a§lParabéns", "§fVocê concluiu todas as missões da semana 1.", 0, 20*3, 0)
                        ActionbarUtil(p).sendActionBarMessage("§aVocê concluiu a missão §f#5§a do passe de elite!", 5, PlayPass.INSTANCE)

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cash setar ${p.name} 20000")
                        passManager.colectQuest("questsCompleter")
                    }
            }
        }
    }

}