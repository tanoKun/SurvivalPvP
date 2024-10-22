package com.github.tanokun.survivalpvp.command.setting

import com.github.tanokun.survivalpvp.game.team.TeamType
import com.github.tanokun.survivalpvp.plguin
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import org.bukkit.Bukkit

object TeamSubCommand {
    init {
        commandAPICommand("setting") {
            withPermission(CommandPermission.OP)
            subcommand("team") {
                subcommand("random") {
                    anyExecutor { _, _ ->
                        plguin.createTeam()

                        SettingCommand.generator.clearPlayers()

                        val bePlayers = Bukkit.getOnlinePlayers().filter { it.name.contains("BE_") }
                        val javaPlayers = Bukkit.getOnlinePlayers().filterNot { it.name.contains("BE_") }

                        bePlayers.shuffled().forEachIndexed { index, player ->
                            val team = if (index < bePlayers.size / 2) TeamType.RED
                            else TeamType.BLUE

                            SettingCommand.generator.setExpectTeam(player.uniqueId, team)

                            plguin.getObjectiveTeam(team).addEntity(player)
                        }

                        javaPlayers.shuffled().forEachIndexed { index, player ->
                            val team = if (index < javaPlayers.size / 2) TeamType.RED
                            else TeamType.BLUE

                            SettingCommand.generator.setExpectTeam(player.uniqueId, team)

                            plguin.getObjectiveTeam(team).addEntity(player)
                        }
                    }
                }
            }
        }
    }
}