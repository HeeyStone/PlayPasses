package com.playfactions.pass.manager

import com.playfactions.pass.PlayPass
import java.util.*
import kotlin.collections.ArrayList

class PassManager(val uuid: UUID) {

    fun putInCache() {
        PlayPass.CACHE.putIfAbsent(uuid, QuestPlayer(uuid, ArrayList()))

        addQuest(Quest("mobKiller", 0))
    }

    fun hasPass() = PlayPass.CACHE.containsKey(uuid)
    fun getQuestPlayer(): QuestPlayer = PlayPass.CACHE[uuid]!!

    fun hasQuestByName(quest: String): Boolean {
        getQuestPlayer().quests.forEach {
            if (it.name.equals(quest, true)) {
                return true
            }
        }
        return false
    }

    fun addQuest(quest: Quest) {
        if (hasPass()) {
            if (!hasQuestByName(quest.name)) {
                getQuestPlayer().quests.add(quest)
            }
        }
    }

    fun getQuestProgress(quest: String): Int? {
        if (hasPass()) {
            getQuestPlayer().quests.forEach {
                if (it.name.equals(quest, true) || it.name.equals("collected$quest", true)) return it.progress
            }
        }
        return null
    }

    fun addProgressInQuest(quest: String, int: Int) {
        if (hasPass()) {
            getQuestPlayer().quests.forEach {
                if (it.name.equals(quest, true) || it.name.toLowerCase().equals("collected${quest.toLowerCase()}")) it.progress += int
            }
        }
    }

    fun colectQuest(quest: String) {
        val quests = getQuestPlayer().quests
        quests.forEach {
            if (it.name == quest) {
                it.name = "collectedMobKiller"
            }
        }
        getQuestPlayer().quests = quests
    }

    fun isCollectedQuest(quest: String): Boolean {
        getQuestPlayer().quests.forEach {
            if (it.name.toLowerCase().contains(quest.toLowerCase()) && it.name.contains("collected")) return true
        }
        return false
    }

}

data class QuestPlayer(val uuid: UUID, var quests: ArrayList<Quest>)
data class Quest(var name: String, var progress: Int)
