package com.playfactions.pass.storage

import java.sql.*
import java.util.Arrays
import java.sql.SQLException
import net.minecraft.server.v1_8_R3.TileEntitySkull.executor
import java.util.stream.Stream
import java.util.stream.IntStream
import java.sql.ResultSet
import net.minecraft.server.v1_8_R3.TileEntitySkull.executor


class MySQL(
    private val host: String,
    private val user: String,
    private val password: String,
    private val database: String
) {

    var connection: Connection? = null
    var statment: Statement? = null

    fun hasConnected(): Boolean {
        if (connection != null && !connection!!.isClosed) return true

        val con = "jdbc:mysql://$host/$database"
        Class.forName("com.mysql.jdbc.Driver")
        runCatching {
            connection = DriverManager.getConnection(con, user, password)
            statment = connection!!.createStatement()
            return true
        }
        return false
    }

    fun prepare(st: String): ResultSet? {
        if (executor.any { st.startsWith(it, true) }) statment?.executeUpdate(st).also { return null }
        return statment?.executeQuery(st)
    }

    fun prepare(query: String, vararg objects: Any): ResultSet? {
        val ps = statement(query)
        objects.forEachIndexed { index, any ->
            ps?.setObject(index + 1, any)
        }
        if (executor.any { query.startsWith(it, true) }) ps?.executeUpdate().also { return null }
        return ps?.executeQuery()
    }

    private fun statement(query: String) = connection?.prepareStatement(query)

    companion object {
        private val executor = arrayOf("update", "insert", "delete", "create")
    }
}
