package com.github.tanokun.survivalpvp.command.setting

import com.github.tanokun.survivalpvp.game.team.TeamType
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import org.bukkit.Material

object SpawnSubCommand {
    init {
        commandAPICommand("setting") {
            withPermission(CommandPermission.OP)
            subcommand("spawn") {
                subcommand("red") {
                    playerExecutor { player, _ ->
                        SettingCommand.generator.setSpawn(TeamType.RED, player.location)
                        player.sendMessage("「§c赤§r」のスポーンを設定しました")

                        player.sendBlockChange(player.location, Material.RED_WOOL, 14)
                    }
                }
                subcommand("blue") {
                    playerExecutor { player, _ ->
                        SettingCommand.generator.setSpawn(TeamType.BLUE, player.location)
                        player.sendMessage("「§9青§r」のスポーンを設定しました")

                        player.sendBlockChange(player.location, Material.BLUE_WOOL, 11)
                    }
                }
            }
        }
    }
}