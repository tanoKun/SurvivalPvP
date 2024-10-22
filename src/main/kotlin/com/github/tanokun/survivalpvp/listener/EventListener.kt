package com.github.tanokun.survivalpvp.listener

import com.github.tanokun.survivalpvp.command.setting.SettingCommand
import com.github.tanokun.survivalpvp.game.BreakResult.*
import com.github.tanokun.survivalpvp.game.gamer
import com.github.tanokun.survivalpvp.plguin
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.PlayerDeathEvent

object EventListener: Listener {
    init {
        Bukkit.getPluginManager().registerEvents(this, plguin)
    }

    @EventHandler
    fun breakBeacon(e: BlockBreakEvent) {
        if (isNotPlaying()) return
        val gamer = e.player.gamer() ?: return

        when (gamer.whatBreakBlock(e.block)) {
            ENEMY_BEACON -> {
                Bukkit.getOnlinePlayers().forEach {
                    it.showTitle(
                        Title.title(Component.text("§7「${gamer.getTeamName()}§7」の勝利"), Component.text(""))
                    )

                    it.gameMode = GameMode.ADVENTURE

                    SettingCommand.game?.stop()
                    SettingCommand.game = null
                }
            }

            TEAM_BEACON, TEAM_WOOL -> e.isCancelled = true
            OTHER -> return
        }
    }

    @EventHandler
    fun kill(e: PlayerDeathEvent) {
        if (isNotPlaying()) return
        val gamer = e.player.gamer() ?: return

        gamer.die()
    }

    private fun isNotPlaying(): Boolean = SettingCommand.game == null
}