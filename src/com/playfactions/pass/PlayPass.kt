package com.playfactions.pass

import com.playfactions.pass.commands.PassCommand
import com.playfactions.pass.listener.PlayerListeners
import com.playfactions.pass.manager.QuestPlayer
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.HashMap

class PlayPass : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: JavaPlugin
        lateinit var CACHE: HashMap<UUID, QuestPlayer>
    }

    override fun onEnable() {
        INSTANCE = this
        CACHE = HashMap()

        PassCommand()
        PlayerListeners()
    }

}