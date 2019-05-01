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
                if (it.name.equals(quest, true)) return it.progress
            }
        }
        return null
    }

    fun addProgressInQuest(quest: String, int: Int) {
        if (hasPass()) {
            getQuestPlayer().quests.forEach {
                if (it.name.equals(quest, true)) it.progress += int
            }
        }
    }

}

data class QuestPlayer(val uuid: UUID, val quests: ArrayList<Quest>)
data class Quest(val name: String, var progress: Int)
