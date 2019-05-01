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

        if (args[0].equals("setar", true)) {
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
        return false
    }

}