package com.playfactions.pass

import com.playfactions.pass.commands.PassCommand
import com.playfactions.pass.listener.InventoryListeners
import com.playfactions.pass.listener.PlayerListeners
import com.playfactions.pass.manager.PassManager
import com.playfactions.pass.manager.PassPlayer
import com.playfactions.pass.storage.MySQL
import com.playfactions.pass.task.SaverTask
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
        SQL = MySQL("${config.getString("mysql.host")}:3306", config.getString("mysql.user"), config.getString("mysql.password"), config.getString("mysql.database"))
        CACHE = HashMap()

        if (SQL.hasConnected()) {
            SQL.prepare("create table if not exists playpasses(UUID VARCHAR(200) PRIMARY KEY, Quests VARCHAR(200) NOT NULL)")
        } else {
            println("Ocorreu um erro ao iniciar o MySQL, desabilitando plugin...")
            server.pluginManager.disablePlugin(this)
        }

        saveDefaultConfig()

        if (server.pluginManager.getPlugin("PlayPasses").isEnabled) {
            PassCommand()
            SaverTask()
            PlayerListeners()
            InventoryListeners()
        }
    }

    override fun onDisable() {
        CACHE.keys.forEach {
            if (PassManager(it).isInSQL()) PassManager(it).save() else PassManager(it).insert()
        }
    }

}