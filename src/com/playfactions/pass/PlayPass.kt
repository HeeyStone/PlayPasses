package com.playfactions.pass

import com.playfactions.pass.commands.PassCommand
import com.playfactions.pass.listener.InventoryListeners
import com.playfactions.pass.listener.PlayerListeners
import com.playfactions.pass.manager.PassManager
import com.playfactions.pass.manager.PassPlayer
import com.playfactions.pass.storage.MySQL
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.HashMap

class PlayPass : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: JavaPlugin
        lateinit var CACHE: HashMap<UUID, PassPlayer>
        lateinit var SQL: MySQL
    }

    override fun onEnable() {
        INSTANCE = this
        CACHE = HashMap()
        SQL = MySQL("localhost:3306", "root", "", "stone")

        if (PassManager.getAllPlayersSQL() != null) {
            for (questPlayer in PassManager.getAllPlayersSQL()!!) {
                CACHE.putIfAbsent(questPlayer.uuid, questPlayer)
            }
        }

        if (SQL.hasConnected()) {
            SQL.prepare("create table if not exists playpasses(UUID VARCHAR(200) PRIMARY KEY, Quests VARCHAR(200) NOT NULL)")
        }

        PassCommand()
        PlayerListeners()
        InventoryListeners()
    }

    override fun onDisable() {
        for (uuid in CACHE.keys) {
            val passManager = PassManager(uuid)
            if (passManager.isInSQL()) {
                passManager.save()
            } else passManager.insert()
        }
    }

}