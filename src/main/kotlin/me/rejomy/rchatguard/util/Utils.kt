package me.rejomy.rchatguard.util

import me.rejomy.rchatguard.INSTANCE
import me.rejomy.rchatguard.caps
import me.rejomy.rchatguard.words
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.regex.Pattern

fun sendTitle(player: Player, subtitle: String) {
    player.sendTitle("&6✕ &eＷＡＲＮ &6✕".replace("&", "§"), subtitle.replace("&", "§"))
}

fun smallDelay(time: Long, delay: Int): Boolean {
    return System.currentTimeMillis() - time < delay * 1000
}

fun sameIpPlayer(player: Player): Player? {
    for (target in Bukkit.getOnlinePlayers())
        if (target != player && target.address == player.address)
            return target
    return null
}

fun capsCheck(text: String): Boolean {
    if (caps && Regex("(\\p{Lu}|\\s){4,}", RegexOption.IGNORE_CASE).containsMatchIn(text))
        return true
    return false
}

fun checkRepeat(message: String): Boolean {
    val length = message.length
    if (length <= 4) return false
    val word = message.split("")
    for (letter in word) {
        var amount = 0

        for (repeat in word)
            if (letter == repeat)
                amount++

        if (length / 2 <= amount)
            return true
    }
    return false
}

fun addViolation(player: Player, type: String, message: String, detect: String) {

    var maxAmount = 0

    val mainSection = INSTANCE.config.getConfigurationSection("violation")
    var violation = INSTANCE.db.getViolation(player.name)
    for (key in mainSection.getKeys(false)) {
        val amount = key.toInt()
        if (amount > maxAmount) maxAmount = amount
        if (amount == violation) {
            for (i in INSTANCE.config.getStringList("violation.$key"))
                Bukkit.getScheduler().scheduleSyncDelayedTask(INSTANCE) {
                    Bukkit.dispatchCommand(
                        Bukkit.getConsoleSender(),
                        i.replace("&", "§").replace("%player", player.name)
                    )
                }
        }
    }

    if (violation > maxAmount)
        INSTANCE.db.delete(player.name)

    log(type, player.name, message, detect)
    violation += 1
    INSTANCE.db.set(player.name, violation)
}

fun arraytostring(massive: List<String>): String {
    var line = ""
    for (i in massive) {
        line = if (line.isEmpty())
            i
        else
            "$line $i"
    }
    return line
}

fun badWord(message: String): String {
    for (word in words) {
        val pattern = Pattern.compile(word, Pattern.CASE_INSENSITIVE or Pattern.MULTILINE)
        if (pattern.matcher(message).find())
            return word
    }
    return ""
}

fun log(type: String, player: String, message: String, detect: String) {

    fun compileMessage(): String {
        val attribute = "§8§m---------------"
        val prefix = "\n§8|§7"
        return "§7\n$attribute$prefix $player detected in $detect$prefix Type: $type $prefix Message: $message\n$attribute\n§7"
    }

    val compileMsg = compileMessage()

    Bukkit.getLogger().info(compileMsg)

    for (receiver in Bukkit.getOnlinePlayers()) {
        if (receiver.hasPermission("rchat.notify"))
            receiver.sendMessage(compileMsg)
    }

}