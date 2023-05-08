package me.rejomy.rchatguard.database

import org.bukkit.Bukkit
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

open class DataBase(clazz: String, val link: String, val insert: String) {

    private var statement: Statement
    private var connection: Connection

    init {
        Class.forName(clazz).newInstance()
        connection = connection()
        statement = connection.createStatement()
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (name varchar(255) PRIMARY KEY, violation TEXT NOT NULL)")
    }

    fun add(player: String, level: Int) {
        statement.executeUpdate("INSERT INTO users (name, violation) VALUES ('$player',$level) $insert violation=$level")
    }

    fun remove(player: String) {
        statement.executeUpdate("DELETE FROM users WHERE name='$player'")
    }

    fun get(player: String): Int? {
        val result = statement.executeQuery("SELECT violation FROM users WHERE name = '$player'")
        return if(result.next()) result.getInt("violation") else 0
    }

    private fun connection(): Connection {
        return DriverManager.getConnection(link)
    }

    fun closeConnection() {
        connection.close()
    }

}