package me.rejomy.rchatguard.listener

import me.rejomy.rchatguard.*
import me.rejomy.rchatguard.util.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class Chat : Listener {

    private val playerOldMessageMap = HashMap<Player, String>()
    private val playerOldMessageDelayMap = HashMap<Player, Long>()

    private fun addChatViolation(player: Player, message: String, detect: String) {
        addViolation(player, "CHAT", message, detect)
    }

    @EventHandler(ignoreCancelled = true)
    fun onChat(event: AsyncPlayerChatEvent) {

        val player = event.player
        if (player.hasPermission("rchat.bypass")) return
        var message = event.message

        fun removeReceivers() {
            for (receiver in Bukkit.getOnlinePlayers())
                if (receiver.name != player.name && sameIpPlayer(player) != receiver)
                    event.recipients.remove(receiver)
        }

        if (playerOldMessageDelayMap.containsKey(player) && smallDelay(
                playerOldMessageDelayMap[player]!!,
                chat_delay
            )
        ) {
            addChatViolation(player, message, "Delay $chat_delay > ${playerOldMessageDelayMap[player]!! / 1000}")
            sendTitle(player, "&7✎ Подождите перед отправкой сообщния!")
        }

        if (autoprivatemsg) {
            val array = message.split(" ")
            if (array.size > 1 && (perm.isNotEmpty() && player.hasPermission(perm) || perm.isEmpty()) && !player.name.equals(array[0]))
                for (target in Bukkit.getOnlinePlayers()) {
                    if (target.name.equals(array[0].replace(",", ""))) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(INSTANCE) {
                            message = message.replace("${array[0].replace(",", "")} ", "")
                            player.chat("/m ${array[0].replace(",", "")} $message")
                        }
                        event.isCancelled = true
                        return
                    }
                }
        }

        if(rep_text && checkRepeat(message)) {
            addChatViolation(player, message, "RepeatLetter")
            sendTitle(player, "&7✎ Ваше сообщение не является целесообразным!")
        }

        if (playerOldMessageMap.containsKey(player) && smallDelay(
                playerOldMessageDelayMap[player]!!,
                10
            ) && StringSimilarity.similarity(
                message,
                playerOldMessageMap[player]!!
            ) > 0.8
        ) {
            addChatViolation(player, message, "StringSimilarity")
            sendTitle(player, "&7✎ Ваше сообщение схоже со старым, перефразируйте его!")
        }

        if (capsCheck(message)) {
            event.message = message.toLowerCase()
            for (po in Bukkit.getOnlinePlayers())
                if (Regex("\\s${po.name}\\s").containsMatchIn(message))
                    event.message = message.toLowerCase().replace(" ${po.name.toLowerCase()}", " ${po.name}")
        }

        val word = badWord(message)

        if (word.isNotEmpty()) {
            addChatViolation(player, message, "REGEX n${words.indexOf(word) + 1}")
            removeReceivers()
            return
        }

        // Проверка на плохие слова с действиями

        playerOldMessageDelayMap[player] = System.currentTimeMillis()
        playerOldMessageMap[player] = message

    }

}