package me.rejomy.rchatguard.command

import me.rejomy.rchatguard.INSTANCE
import me.rejomy.rchatguard.Settings
import me.rejomy.rchatguard.dataBase
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
                    dataBase.closeConnection()
                    INSTANCE.initDataBase()
                    sender.sendMessage("Плагин перезагружен!")
                }
                "remove", "delete" -> {
                    if(args.size == 2) {
                        dataBase.remove(args[1])
                        sender.sendMessage("Игрок ${args[1]} был стёрт из базы!")
                    } else
                        sender.sendMessage("Удаление игрока требует такой синтаксис: /cg remove НИК")
                }

                "vl" -> {
                    if(args.size == 2)
                       sender.sendMessage(dataBase.get(args[1]).toString())
                    else
                        sender.sendMessage("Просмотр нарушений игрока требует такой синтаксис: /cg vl НИК")
                }

                "set" -> {
                    if(args.size == 3) {
                        dataBase.add(args[1], args[2].toInt())
                        sender.sendMessage("Игроку ${args[1]} установлено ${args[2]}")
                    } else
                        sender.sendMessage("Установка нарушений игроку: /cg set НИК кол-во")
                }

            }
        } else
            sender.sendMessage("/cg remove|delete|reload|vl|set")
        return false
    }
}