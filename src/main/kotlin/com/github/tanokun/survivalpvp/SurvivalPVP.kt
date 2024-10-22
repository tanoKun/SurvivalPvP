package com.github.tanokun.survivalpvp

import com.github.tanokun.survivalpvp.command.setting.SettingCommand
import com.github.tanokun.survivalpvp.game.team.TeamType
import com.github.tanokun.survivalpvp.listener.EventListener
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.Team

lateinit var plguin: SurvivalPVP private set

class SurvivalPVP : JavaPlugin() {
    private val teams = hashMapOf<TeamType, Team>()

    override fun onEnable() {
        plguin = this

        createTeam()
        SettingCommand
        EventListener
    }

    override fun onDisable() {
        SettingCommand.game?.stop()
    }

    fun getObjectiveTeam(teamType: TeamType): Team = teams[teamType] ?: throw NullPointerException("")

    fun createTeam() {
        val manager = Bukkit.getScoreboardManager()
        val board = manager.mainScoreboard

        deleteTeam()

        TeamType.entries.forEach { teamType ->
            board.registerNewTeam(teamType.name).also {
                it.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS)
                it.prefix(Component.text("${teamType.prefix}_"))

                teams[teamType] = it
            }
        }
    }

    private fun deleteTeam() {
        val manager = Bukkit.getScoreboardManager()
        val board = manager.mainScoreboard

        TeamType.entries.forEach { teamType ->
            board.getTeam(teamType.name)?.unregister()
        }
    }
}