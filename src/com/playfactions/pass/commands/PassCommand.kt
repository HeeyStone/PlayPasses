package com.playfactions.pass.commands

import com.playfactions.kotlin.util.sendMessage
import com.playfactions.pass.PlayPass
import com.playfactions.pass.manager.PassManager
import com.playfactions.pass.util.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PassCommand : CommandExecutor {

    companion object {
        val list = listOf("setar", "set", "inventory")

        fun CommandSender.openQuestsInventory() {
            val inv = Bukkit.createInventory(null, 3*9, "Missões do passe de elite")

            if (this is Player) {
                val passManager = PassManager(uniqueId)

                inv.setItem(10, ItemStack(if (passManager.isCollectedQuest("mobKiller")) Material.STORAGE_MINECART else Material.MINECART)
                    .name("§eMissão §f#1")
                    .lore(
                        "",
                        "§7Mate §f1.000§7 monstros no servidor.",
                        "§7Você já matou §f${passManager.getQuestProgress("mobKiller")} monstros§7.",
                        "",
                        "§eRecompensa: §f20.000 cash",
                        if (passManager.isCollectedQuest("mobKiller")) "§eVocê já completou essa missão."
                        else if (passManager.getQuestProgress("mobKiller")!! >= 1000) "§eClique para completar a missão"
                        else "§7Você não pode completar a missão",
                        ""))

                inv.setItem(11, ItemStack(if (passManager.isCollectedQuest("blockWalker")) Material.STORAGE_MINECART else Material.MINECART)
                    .name("§eMissão §f#2")
                    .lore(
                        "",
                        "§7Caminhe §f10.000§7 blocos no servidor.",
                        "§7Você já caminhou §f${passManager.getQuestProgress("blockWalker")} blocos§7.",
                        "",
                        "§eRecompensa: §f20.000 cash",
                        if (passManager.isCollectedQuest("blockWalker")) "§eVocê já completou essa missão."
                        else if (passManager.getQuestProgress("blockWalker")!! >= 1000) "§eClique para completar a missão"
                        else "§7Você não pode completar a missão",
                        ""))

                inv.setItem(12, ItemStack(if (passManager.isCollectedQuest("blockBreaker")) Material.STORAGE_MINECART else Material.MINECART)
                    .name("§eMissão §f#3")
                    .lore(
                        "",
                        "§7Quebre §f30.000§7 blocos no servidor.",
                        "§7Você já quebrou §f${passManager.getQuestProgress("blockBreaker")} blocos§7.",
                        "",
                        "§eRecompensa: §f20.000 cash",
                        if (passManager.isCollectedQuest("blockBreaker")) "§eVocê já completou essa missão."
                        else if (passManager.getQuestProgress("blockBreaker")!! >= 30000) "§eClique para completar a missão"
                        else "§7Você não pode completar a missão",
                        ""))
                inv.setItem(13, ItemStack(if (passManager.isCollectedQuest("playerKiller")) Material.STORAGE_MINECART else Material.MINECART)
                    .name("§eMissão §f#4")
                    .lore(
                        "",
                        "§7Mate §f50§7 pessoas no servidor.",
                        "§7Você já matou §f${passManager.getQuestProgress("playerKiller")} pessoas§7.",
                        "",
                        "§eRecompensa: §f20.000 cash",
                        if (passManager.isCollectedQuest("playerKiller")) "§eVocê já completou essa missão."
                        else if (passManager.getQuestProgress("playerKiller")!! >= 50) "§eClique para completar a missão"
                        else "§7Você não pode completar a missão",
                        ""))

                inv.setItem(15, ItemStack(Material.STORAGE_MINECART)
                    .name("§eMissão §f#5")
                    .lore(
                        "",
                        "§7Complete §f4§7 missões do passe de elite do servidor.",
                        "§7Você já completou §f${passManager.getPassPlayer().quests.size} missões§7.",
                        "",
                        "§eRecompensa: §f20.000 cash",
                        if (passManager.isCollectedQuest("questsCompleter")) "§eVocê já completou essa missão."
                        else if (passManager.getQuestProgress("questsCompleter")!! >= 4) "§eClique para completar a missão"
                        else "§7Você não pode completar a missão",
                        ""))

                openInventory(inv)
            }
        }
    }

    init {
        PlayPass.INSTANCE.getCommand("pass").executor = this
    }

    override fun onCommand(sender: CommandSender, command: Command?, label: String?, args: Array<out String>): Boolean {
        if (args.isEmpty() || list.firstOrNull { it == args[0] } == null) {
            sender.sendMessage(
                Sound.VILLAGER_NO,
                "",
                "§6§lPASSE DE ELITE",
                "§7/passe setar <jogador> - Seta um passe.",
                "§7/passe inventory [jogador] - Abre o inventário de missões.",
                "")
            return true
        }

        if (args[0].equals("setar", true) || args[0].equals("set", true)) {
            if (args.size <= 1) {
                sender.sendMessage(Sound.VILLAGER_NO,
                    "",
                    "§6§lPASSE DE ELITE",
                    "§7Use §6'/passe setar <jogador>'§7 para poder setar o passe em alguém.",
                    "")
                return false
            }
            val target = Bukkit.getPlayer(args[1])
            if (!sender.hasPermission("playpasses.setar")) {
                sender.sendMessage(Sound.VILLAGER_NO, "§cVocê não tem permissão para esse comando.")
                return false
            }
            if (target == null) {
                sender.sendMessage(Sound.VILLAGER_NO, "§cO jogador digitado não está on-line.")
                return false
            }
            val passManager = PassManager(target.uniqueId)
            if (passManager.hasPass()) {
                sender.sendMessage(Sound.VILLAGER_NO, "§cO jogador digitado já possui um passe.")
                return false
            }
            passManager.putInCache()
            sender.sendMessage(Sound.LEVEL_UP, "§aVocê setou o passe de §7${target.name}§a com sucesso.")
            target.sendMessage(
                Sound.ITEM_PICKUP,
                "",
                "§6§lPASSE DE ELITE",
                "§7Você ganhou um passe de elite.",
                "§7Você pode fazer as missões do passe no NPC do spawn.",
                "")
            return true
        }
        if (args[0].equals("inventory", true)) {
            if (args.size == 1) {
                if (!PassManager((sender as Player).uniqueId).hasPass()) {
                    sender.sendMessage(Sound.VILLAGER_NO, "§cVocê não possui passe de elite.")
                    return false
                }
                sender.openQuestsInventory()
                return true
            }
            val target = Bukkit.getPlayer(args[1])
            if (target == null) {
                sender.sendMessage(Sound.VILLAGER_NO, "§cO jogador digitado não está on-line.")
                return false
            }
            if (!PassManager(target.uniqueId).hasPass()) {
                sender.sendMessage(Sound.VILLAGER_NO, "§cO jogador digitado não possui passe.")
            }
            target.openQuestsInventory()
            return true
        }

        return false
    }

}