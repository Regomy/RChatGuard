package me.rejomy.rchatguard

import me.rejomy.rchatguard.command.RChatGuard
import me.rejomy.rchatguard.database.DataBase
import me.rejomy.rchatguard.database.MySQL
import me.rejomy.rchatguard.database.SQLite
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

lateinit var INSTANCE: Main
lateinit var dataBase: DataBase

class Main : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()
        INSTANCE = this
        Settings()
        getCommand("rchatguard").executor = RChatGuard()
        initDataBase()
    }

    fun initDataBase() {
        dataBase = when (INSTANCE.config.getString("database.type")) {
            "sqlite" -> SQLite()
            "mysql" -> MySQL()
            else -> {
                Bukkit.getPluginManager().disablePlugin(INSTANCE)
                println(
                    "----------------------------------\n" +
                            "> INCORRECT TYPE - ChatGuard/config.yml:database:type\n" +
                            "> Please select type sqlite or mysql\n" +
                            "> Plugin disable automatically..." +
                            "\n----------------------------------"
                )
                return
            }
        }
    }

    override fun onDisable() {
        dataBase.closeConnection()
    }

}
