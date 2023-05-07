package me.rejomy.rchatguard.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import kotlin.concurrent.thread

class DataBase {

    private val url: String = "jdbc:sqlite:plugins/RChatGuard/users.db"
    private var statement: Statement
    private var connection: Connection

    init {
        Class.forName("org.sqlite.JDBC").newInstance()
        connection = connection()
        statement = connection.createStatement()
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users ('name' TEXT, 'violation' TEXT)")
    }

    fun set(name: String, violation: Int) {
        statement.executeUpdate("INSERT INTO users VALUES ('$name', '$violation')")
    }

    fun delete(name: String) {
        statement.executeUpdate("DELETE FROM users WHERE name='$name'")
    }

    private fun connection(): Connection {
        return DriverManager.getConnection(url)
    }

    fun getViolation(name: String): Int {
        val result: ResultSet =
            connection.createStatement().executeQuery("SELECT COUNT(*) FROM users WHERE name = '$name'")
        return result.getInt(1)
    }

}