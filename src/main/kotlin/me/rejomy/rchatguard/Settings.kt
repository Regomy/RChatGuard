package me.rejomy.rchatguard

import me.rejomy.rchatguard.listener.Chat
import me.rejomy.rchatguard.listener.Command
import org.bukkit.Bukkit

var chat_delay = 0.0
var command_delay = 0.0
var command_check = mutableListOf<String>()
var command_blacklist = false
var old_msg = false
var caps = false
var rep_text = false
var words = mutableListOf<String>()
var autoprivatemsg = false
var perm = ""

class Settings {
    init {

        if (search("Chat.enable")) {

            chat_delay = INSTANCE.config.getDouble("Chat.delay")

            Bukkit.getPluginManager().registerEvents(Chat(), INSTANCE)

            if (search("auto private msg.enable")) {
                autoprivatemsg = true
                perm = INSTANCE.config.getString("auto private msg.has permission")
            }

        }

        if (search("Command.enable")) {
            command_delay = INSTANCE.config.getDouble("Command.delay")

            Bukkit.getPluginManager().registerEvents(Command(), INSTANCE)

            if (search("Command.black list"))
                command_blacklist = true

            for (cmd in INSTANCE.config.getStringList("Command.check command"))
                command_check.add(cmd)
        }

        caps = search("block.caps")
        old_msg = search("block.old msg")
        rep_text = search("block.repeated text")

        if (search("block.bad word"))
            for (word in INSTANCE.config.getStringList("list word"))
                words.add(word)

    }

    private fun search(path: String): Boolean {
        return INSTANCE.config.getBoolean(path)
    }

}