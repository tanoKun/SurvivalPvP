package com.github.tanokun.survivalpvp.game.viewer

import com.github.tanokun.survivalpvp.game.gamer
import com.github.tanokun.survivalpvp.game.team.TeamType
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class DataViewer: BukkitRunnable() {
    override fun run() {
        val red = Bukkit.getOnlinePlayers()
            .filter {
                val gamer = it.gamer() ?: return@filter false
                !gamer.isDied && gamer.isInTeamOf(TeamType.RED)
            }.count()

        val blue = Bukkit.getOnlinePlayers()
            .filter {
                val gamer = it.gamer() ?: return@filter false
                !gamer.isDied && gamer.isInTeamOf(TeamType.BLUE)
            }.count()

        Bukkit.getOnlinePlayers().forEach {
            it.sendActionBar(Component.text("§c赤の残り人数:§f $red, §9青の残り人数:§f $blue"))
        }
    }
}