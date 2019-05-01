package com.playfactions.pass.manager

import com.playfactions.pass.PlayPass
import java.util.*
import kotlin.collections.ArrayList

class PassManager(val uuid: UUID) {

    fun putInCache() = PlayPass.CACHE.putIfAbsent(uuid, QuestPlayer(uuid, ArrayList()))
    fun hasPass() = PlayPass.CACHE.containsKey(uuid)

}

data class QuestPlayer(val uuid: UUID, val quests: List<Quest>)
data class Quest(val name: String, val int: Int)