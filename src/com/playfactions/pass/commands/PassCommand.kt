package com.playfactions.pass.commands

import com.playfactions.kotlin.util.sendMessage
import com.playfactions.pass.PlayPass
import com.playfactions.pass.manager.PassManager
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class PassCommand : CommandExecutor {

    companion object {
        val list = listOf("setar", "set", "inventory")
    }

    init {
        PlayPass.INSTANCE.getCommand("manager").executor = this
    }

    override fun onCommand(sender: CommandSender, command: Command?, label: String?, args: Array<out String>): Boolean {
        if (args.isEmpty() || list.firstOrNull { it == args[0] } == null) {
            sender.sendMessage(
                Sound.VILLAGER_NO,
                "",
                "§6§lPASSE DE ELITE",
                "§f/passe setar <jogador> - Seta um passe.",
                "§f/passe inventory [jogador] - Abre o inventário de missões.",
                "")
            return true
        }

        if (args[0].equals("setar", true)) {
            val target = Bukkit.getPlayer(args[0])
            val passManager = PassManager(target.uniqueId)
            if (!sender.hasPermission("playpasses.setar")) {
                sender.sendMessage(Sound.VILLAGER_NO, "§cVocê não tem permissão para esse comando.")
                return false
            }
            if (target == null) {
                sender.sendMessage(Sound.VILLAGER_NO, "§cO jogador digitado não está on-line.")
                return false
            }
            if (passManager.hasPass()) {
                sender.sendMessage(Sound.VILLAGER_NO, "§cO jogador digitado já possui um passe.")
            }
            passManager.putInCache()
            sender.sendMessage(Sound.LEVEL_UP, "§aVocê setou o passe de §f${target.name}§a com sucesso.")
            target.sendMessage(
                Sound.ITEM_PICKUP,
                "",
                "§6§lPASSE DE ELITE",
                "§fVocê ganhou um passe de elite.",
                "§fVocê pode fazer as missões do passe no NPC do spawn.",
                "")
            return true
        }
        return false
    }

}