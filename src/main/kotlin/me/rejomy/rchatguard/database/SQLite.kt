package me.rejomy.rchatguard.database

import org.bukkit.Bukkit

class SQLite : DataBase("org.sqlite.JDBC", "jdbc:sqlite:plugins/RChatGuard/users.db", "ON CONFLICT(name) DO UPDATE SET") {

    override fun add(player: String, level: Int) {
        statement.executeUpdate("INSERT OR REPLACE INTO users (name, violation) VALUES ('$player', $level)")
    }

}