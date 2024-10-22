package com.github.tanokun.survivalpvp.command.setting

import com.github.tanokun.survivalpvp.game.team.TeamType
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import org.bukkit.Material

object BeaconSubCommand {
    init {
        commandAPICommand("setting") {
            withPermission(CommandPermission.OP)
            subcommand("beacon") {
                subcommand("red") {
                    playerExecutor { player, _ ->
                        val loc = player.getTargetBlock(setOf(Material.AIR), 40).location
                        SettingCommand.generator.setBeacon(TeamType.RED, loc)
                        player.sendMessage("§7「§c赤§7」のビーコンの位置を設定しました")

                        player.sendBlockChange(loc, Material.RED_CONCRETE, 14)
                    }
                }
                subcommand("blue") {
                    playerExecutor { player, _ ->
                        val loc = player.getTargetBlock(setOf(Material.AIR), 40).location
                        SettingCommand.generator.setBeacon(TeamType.BLUE, loc)
                        player.sendMessage("§7「§9青§7」のビーコンの位置を設定しました")

                        player.sendBlockChange(loc, Material.BLUE_CONCRETE, 11)
                    }
                }
            }
        }
    }
}