package com.github.tanokun.survivalpvp.game.team

import com.github.tanokun.survivalpvp.game.GamePlayer
import com.github.tanokun.survivalpvp.game.gamer
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

class GameTeam(
    private val spawn: Location,
    private val beacon: Location,
    val teamType: TeamType
) {

    fun setupBeacon() {
        beacon.world.getBlockAt(beacon).setType(Material.BEACON, true)
        beacon.world.getBlockAt(beacon.clone().add(0.0, -1.0, 0.0)).setType(teamType.underBlock, true)
    }

    fun deleteBeacon() {
        beacon.world.getBlockAt(beacon).setType(Material.AIR, true)
        beacon.world.getBlockAt(beacon.clone().add(0.0, -1.0, 0.0)).setType(Material.AIR, true)
    }

    fun teleportSpawn(player: Player) {
        player.teleport(spawn)
    }

    fun hasBeaconLocationOf(location: Location) = beacon.toBlockLocation() == location.toBlockLocation()

    fun hasBeaconWoolLocationOf(location: Location) = beacon.clone().add(0.0, -1.0, 0.0).toBlockLocation() == location.toBlockLocation()

    fun registerTeammate(player: Player) {
        player.gamer(GamePlayer(this, player.uniqueId))
    }
}

