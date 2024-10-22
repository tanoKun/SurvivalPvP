package com.github.tanokun.survivalpvp.command.setting

import com.github.tanokun.survivalpvp.game.GameGenerator
import com.github.tanokun.survivalpvp.game.SurvivalGame
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.kotlindsl.*
import org.bukkit.command.CommandSender

object SettingCommand {

    var game: SurvivalGame? = null

    var generator: GameGenerator = GameGenerator()

    init {
        SpawnSubCommand
        BeaconSubCommand
        TeamSubCommand

        commandAPICommand("setting") {
            withPermission(CommandPermission.OP)
            subcommand("start") {
                anyExecutor(::start)
            }

            subcommand("center") {
                playerExecutor { player, _ ->
                    generator.center = player.location
                    player.sendMessage("§7ボーダーセンターを設定しました")
                }
            }

            subcommand("centerRadius") {
                integerArgument("radius", 0, 100000)
                playerExecutor { player, args ->
                    generator.centerRadius = args.getOptional(0).get() as Int
                    player.sendMessage("§7ボーダーの半径を「${generator.centerRadius}」設定しました")
                }
            }
        }
    }

    private fun start(sender: CommandSender, args: CommandArguments) {
        if (game != null) {
            sender.sendMessage("§cゲームが既に開始されてしまっています")
            return
        }

        try {
            val survivalGame = generator.build()
            survivalGame.start()

            game = survivalGame
        } catch (e: Exception) {
            sender.sendMessage("§7${e.message}")
        }
    }
}