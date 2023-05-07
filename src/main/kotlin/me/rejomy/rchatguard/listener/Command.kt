package me.rejomy.rchatguard.listener

import me.rejomy.rchatguard.*
import me.rejomy.rchatguard.util.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class Command : Listener {

    private val playerOldMessageMap = HashMap<Player, String>()
    private val playerOldMessageDelayMap = HashMap<Player, Long>()

    private fun addCommandViolation(player: Player, message: String, detect: String) {
        addViolation(player, "COMMAND", message, detect)
    }

    @EventHandler(ignoreCancelled = true)
    fun onCommand(event: PlayerCommandPreprocessEvent) {

        val player = event.player
        if(player.hasPermission("rchat.bypass")) return
        val command = event.message
        var msg = command.split(" ")
        val blist = contains(command_check, msg[0])
        if (command_blacklist && !blist || !command_blacklist && blist) return
        msg = msg.drop(1)
        var target = false
        if(Bukkit.getPlayer(msg[0]) != null) {
            msg = msg.drop(1)
            target = true
        }
        val message = arraytostring(msg)

        if (playerOldMessageDelayMap.containsKey(player) && smallDelay(playerOldMessageDelayMap[player]!!, command_delay))
            addCommandViolation(player, command, "Delay $chat_delay > ${playerOldMessageDelayMap[player]!! / 1000}")

        if (capsCheck(message)) {
            event.message = event.message.toLowerCase()
            for (po in Bukkit.getOnlinePlayers())
                if (Regex("\\s${po.name}\\s").containsMatchIn(message))
                    event.message = event.message.toLowerCase().replace(" ${po.name.toLowerCase()} ", " ${po.name} ")
        }

        if(rep_text && checkRepeat(message)) {
            addCommandViolation(player, message, "RepeatLetter")
            sendTitle(player, "&7✎ Ваше сообщение не является целесообразным!")
        }

        if (playerOldMessageMap.containsKey(player) && smallDelay(playerOldMessageDelayMap[player]!!, 10)
            && StringSimilarity.similarity(message, playerOldMessageMap[player]!!) > 0.8
        )
            addCommandViolation(player, message, "StringSimilarity")

        val word = badWord(message)

        if (word.isNotEmpty()) {
            addCommandViolation(player, message, "REGEX n${words.indexOf(word) + 1}")
            if(!target || Bukkit.getPlayer(msg[1]) != null && sameIpPlayer(player) != Bukkit.getPlayer(msg[1]))
                event.isCancelled = true
        }

        playerOldMessageDelayMap[player] = System.currentTimeMillis()
        playerOldMessageMap[player] = message
    }

    fun contains(list: MutableList<String>, word: String): Boolean {
        for (i in list)
            if (word == "/$i") return true
        return false
    }

}