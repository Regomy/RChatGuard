package me.rejomy.rchatguard.database

class SQLite : DataBase("org.sqlite.JDBC", "jdbc:sqlite:plugins/RChatGuard/users.db", "ON CONFLICT(name) DO UPDATE SET")