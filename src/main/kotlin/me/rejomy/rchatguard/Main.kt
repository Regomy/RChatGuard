package me.rejomy.rchatguard

import me.rejomy.rchatguard.command.RChatGuard
import me.rejomy.rchatguard.database.DataBase
import org.bukkit.plugin.java.JavaPlugin

lateinit var INSTANCE: Main

class Main: JavaPlugin() {

    lateinit var db: DataBase

    override fun onEnable() {
        saveDefaultConfig()
        INSTANCE = this
        Settings()
        db = DataBase()
        getCommand("rchatguard").executor = RChatGuard()
    }

}
