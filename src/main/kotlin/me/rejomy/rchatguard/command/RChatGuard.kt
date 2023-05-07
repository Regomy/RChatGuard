package me.rejomy.rchatguard.command

import me.rejomy.rchatguard.INSTANCE
import me.rejomy.rchatguard.Settings
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class RChatGuard: CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command?,
        label: String?,
        args: Array<out String>
    ): Boolean {
        if(args.isNotEmpty()) {

            when(args[0]) {
                "reload", "релоад" -> {
                    INSTANCE.saveDefaultConfig()
                    Settings()
                }
                "remove", "delete" -> {
                    if(args.size == 2)
                        INSTANCE.db.delete(args[1])
                    else
                        sender.sendMessage("Удаление игрока требует такой синтаксис: /cg remove НИК")
                }

            }
        } else {
            sender.sendMessage("/cg remove|delete|reload")
        }
        return false
    }
}