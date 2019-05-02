package com.playfactions.pass.task

import com.playfactions.pass.PlayPass
import com.playfactions.pass.manager.PassManager
import org.bukkit.scheduler.BukkitRunnable

class SaverTask : BukkitRunnable() {

    init {
        runTaskTimerAsynchronously(PlayPass.INSTANCE, 0, 20*30)
    }

    override fun run() {
        if (PlayPass.SQL.hasConnected()) {
            PlayPass.CACHE.keys.forEach {
                if (PassManager(it).isInSQL()) PassManager(it).save() else PassManager(it).insert()
            }
        }
    }

}