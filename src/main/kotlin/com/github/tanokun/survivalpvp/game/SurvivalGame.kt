package com.github.tanokun.survivalpvp.game

import com.github.tanokun.survivalpvp.game.team.GameTeam
import com.github.tanokun.survivalpvp.game.viewer.DataViewer
import com.github.tanokun.survivalpvp.plguin
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.scheduler.BukkitRunnable

class SurvivalGame(
    private val gameTeams: List<GameTeam>,
    private val borderCenter: Location,
    private val borderRadius: Int
) {

    private val viewer: DataViewer = DataViewer()

    private val soundAnvil = Sound.sound(NamespacedKey.minecraft("block.anvil.place"), Sound.Source.PLAYER, 3f, 1f)
    private val soundExplosion = Sound.sound(NamespacedKey.minecraft("entity.generic.explode"), Sound.Source.PLAYER, 3f, 1f)

    fun start() {
        gameTeams.forEach { it.setupBeacon() }

        borderCenter.world.worldBorder.apply {
            center = borderCenter
            size = borderRadius.toDouble() * 2
        }

        var timer = 3
        object : BukkitRunnable() {
            override fun run() {
                if (timer != 0) Bukkit.getOnlinePlayers().forEach { it.playSound(soundAnvil) }

                if (timer == 0) {
                    Bukkit.getOnlinePlayers().forEach {
                        it.gamer()?.spawn()
                        it.playSound(soundExplosion)
                    }

                    cancel()
                }

                timer--
            }
        }.runTaskTimer(plguin, 0, 20)

        viewer.runTaskTimer(plguin, 1, 5)
    }

    fun stop() {
        gameTeams.forEach { it.deleteBeacon() }
        viewer.cancel()

        borderCenter.world.worldBorder.apply {
            center = borderCenter
            size = maxSize
        }
    }
}