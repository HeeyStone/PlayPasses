package com.playfactions.pass.manager

import com.playfactions.pass.PlayPass
import java.util.*
import kotlin.collections.ArrayList

class PassManager(val uuid: UUID) {

    val sql = PlayPass.SQL

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
                if (it.name.equals(
                        quest,
                        true
                    ) || it.name.toLowerCase().equals("collected${quest.toLowerCase()}")
                ) it.progress += int
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

    fun insert() {
        if (!isInSQL()) {
            val serialized = "{mobKiller:${getQuestProgress("mobKiller")};blockWalker:${getQuestProgress("blockWalker")};blockBreaker:${getQuestProgress("blockBreaker")};playerKiller:${getQuestProgress("playerKiller")};questsCompleter:${getQuestProgress("questsCompleter")}}}"
            sql.prepare("INSERT INTO playpasses (UUID, Quests) VALUES (?, ?)", uuid.toString(), serialized)
        }
    }

    fun save() {
        if (isInSQL()) {
            val serialized = "{mobKiller:${getQuestProgress("mobKiller")};blockWalker:${getQuestProgress("blockWalker")};blockBreaker:${getQuestProgress("blockBreaker")};playerKiller:${getQuestProgress("playerKiller")};questsCompleter:${getQuestProgress("questsCompleter")}}}"
            sql.prepare("UPDATE playpasses SET UUID = ?, Quests = ?", uuid.toString(), serialized)
        }
    }

    fun isInSQL(): Boolean {
        if (sql.prepare("SELECT * FROM playpasses WHERE UUID = ?", uuid.toString())?.next()!!) {
            return true
        }
        return false
    }

    companion object {
        private fun finalSplit(string: String, index: Int) = string.replace("}", "").replace("{", "").split(";")[index].split(":")

        fun getAllPlayersSQL(): List<PassPlayer> {
            val rs = PlayPass.SQL.prepare("SELECT * FROM playpasses")
            val listPlayer = ArrayList<PassPlayer>()
            val listQuests = ArrayList<Quest>()

            while (rs?.next()!!) {
                listQuests.add(Quest(finalSplit(rs.getString("Quests"), 0)[0], finalSplit(rs.getString("Quests"), 0)[1].toInt()))
                listQuests.add(Quest(finalSplit(rs.getString("Quests"), 1)[0], finalSplit(rs.getString("Quests"), 1)[1].toInt()))
                listQuests.add(Quest(finalSplit(rs.getString("Quests"), 2)[0], finalSplit(rs.getString("Quests"), 2)[1].toInt()))
                listQuests.add(Quest(finalSplit(rs.getString("Quests"), 3)[0], finalSplit(rs.getString("Quests"), 3)[1].toInt()))
                listQuests.add(Quest(finalSplit(rs.getString("Quests"), 4)[0], finalSplit(rs.getString("Quests"), 4)[1].toInt()))

                listPlayer.add(PassPlayer(UUID.fromString(rs.getString("UUID")), listQuests))
            }
            return listPlayer
        }
    }

}

data class PassPlayer(val uuid: UUID, var quests: ArrayList<Quest>)
data class Quest(var name: String, var progress: Int)
