package me.rejomy.rchatguard.database

import me.rejomy.rchatguard.INSTANCE

class MySQL : DataBase("com.mysql.jdbc.Driver", "jdbc:mysql://" +
        "${INSTANCE.config.getString("database.mysql.host")}:" +
        "${INSTANCE.config.getString("database.mysql.port")}/" +
        INSTANCE.config.getString("database.mysql.db name") +
        "?user=${INSTANCE.config.getString("database.mysql.user name")}" +
        "&password=${INSTANCE.config.getString("database.mysql.password")}", "ON DUPLICATE KEY UPDATE")