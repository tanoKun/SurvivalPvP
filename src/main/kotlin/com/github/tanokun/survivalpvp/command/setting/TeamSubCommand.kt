package com.github.tanokun.survivalpvp.command.setting

import com.github.tanokun.survivalpvp.game.team.TeamType
import com.github.tanokun.survivalpvp.plguin
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.kotlindsl.*
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player

object TeamSubCommand {
    private val soundExperience = Sound.sound(NamespacedKey.minecraft("entity.experience_orb.pickup"), Sound.Source.PLAYER, 3f, 1f)

    init {
        commandAPICommand("setting") {
            withPermission(CommandPermission.OP)
            subcommand("team") {
                subcommand("random") {
                    anyExecutor { sender, _ ->
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

                        sender.playSound(soundExperience)
                        sender.sendMessage("§7チームをシャッフルしました")
                    }
                }
                subcommand("add") {
                    playerArgument("target")
                    multiLiteralArgument("team", literals = TeamType.entries.map { it.name }.toTypedArray())
                    anyExecutor { sender, args ->
                        val player = args.getOptionalByClass(0, Player::class.java).get()
                        val team = TeamType.valueOf(args.getOrDefaultUnchecked(1, "BLUE"))

                        TeamType.entries.forEach { plguin.getObjectiveTeam(it).removeEntity(player) }
                        plguin.getObjectiveTeam(team).addEntity(player)

                        sender.playSound(soundExperience)
                        sender.sendMessage("§7「${player.name}」を「${team.display}§7」に追加しました")
                    }
                }
            }
        }
    }
}