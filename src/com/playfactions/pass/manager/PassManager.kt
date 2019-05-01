package com.playfactions.pass.manager

import com.playfactions.pass.PlayPass
import java.util.*
import kotlin.collections.ArrayList

class PassManager(val uuid: UUID) {

    fun putInCache() {
        PlayPass.CACHE.putIfAbsent(uuid, PassPlayer(uuid, ArrayList()))

        addQuest(Quest("mobKiller", 0))
        addQuest(Quest("blockWalker", 0))
        addQuest(Quest("blockBreaker", 0))
        addQuest(Quest("playerKiller", 0))
        addQuest(Quest("questsCompleter", 0))
    }

    fun hasPass() = PlayPass.CACHE.containsKey(uuid)
    fun getPassPlayer(): PassPlayer = PlayPass.CACHE[uuid]!!

    fun hasQuestByName(quest: String): Boolean {
        getPassPlayer().quests.forEach {
            if (it.name.equals(quest, true)) {
                return true
            }
        }
        return false
    }

    fun addQuest(quest: Quest) {
        if (hasPass()) {
            if (!hasQuestByName(quest.name)) {
                getPassPlayer().quests.add(quest)
            }
        }
    }

    fun getQuestProgress(quest: String): Int? {
        if (hasPass()) {
            getPassPlayer().quests.forEach {
                if (it.name.equals(quest, true) || it.name.equals("collected$quest", true)) return it.progress
            }
        }
        return null
    }

    fun addProgressInQuest(quest: String, int: Int) {
        if (hasPass()) {
            getPassPlayer().quests.forEach {
                if (it.name.equals(quest, true) || it.name.toLowerCase().equals("collected${quest.toLowerCase()}")) it.progress += int
            }
        }
    }

    fun colectQuest(quest: String) {
        val quests = getPassPlayer().quests
        quests.forEach {
            if (it.name == quest) {
                it.name = "collected$quest"
            }
        }
        getPassPlayer().quests = quests
    }

    fun isCollectedQuest(quest: String): Boolean {
        getPassPlayer().quests.forEach {
            if (it.name.toLowerCase().contains(quest.toLowerCase()) && it.name.contains("collected")) return true
        }
        return false
    }
    

}

data class PassPlayer(val uuid: UUID, var quests: ArrayList<Quest>)
data class Quest(var name: String, var progress: Int)
