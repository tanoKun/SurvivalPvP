package com.github.tanokun.survivalpvp.game

import com.github.tanokun.survivalpvp.game.team.GameTeam
import com.github.tanokun.survivalpvp.game.team.TeamType
import com.github.tanokun.survivalpvp.plguin
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import java.util.*

private const val GAMER = "gamer"

class GamePlayer(
    private val gameTeam: GameTeam,
    private val uuid: UUID
) {
    var isDied = false
        private set

    fun spawn() {
        Bukkit.getPlayer(uuid)?.let {
            gameTeam.teleportSpawn(it)
            it.gameMode = GameMode.SURVIVAL
            it.inventory.clear()
        }

    }

    fun die() {
        isDied = true
        Bukkit.getPlayer(uuid)?.let {
            it.gameMode = GameMode.SPECTATOR
            it.inventory.clear()
        }
    }

    fun isInTeamOf(teamType: TeamType) = gameTeam.teamType == teamType

    fun whatBreakBlock(block: Block): BreakResult {
        if (gameTeam.hasBeaconWoolLocationOf(block.location)) return BreakResult.TEAM_WOOL
        if (block.type != Material.BEACON) return BreakResult.OTHER
        if (gameTeam.hasBeaconLocationOf(block.location)) return BreakResult.TEAM_BEACON

        return BreakResult.ENEMY_BEACON
    }

    fun getTeamName() = gameTeam.teamType.display
}

fun Player.gamer(): GamePlayer? {
    return (this.getMetadata(GAMER).firstOrNull()?.value() ?: return null) as GamePlayer
}

fun Player.gamer(gamePlayer: GamePlayer) {
    this.setMetadata(GAMER, FixedMetadataValue(plguin, gamePlayer))
}