package com.github.tanokun.survivalpvp.game

import com.github.tanokun.survivalpvp.game.team.GameTeam
import com.github.tanokun.survivalpvp.game.team.TeamType
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

class GameGenerator {
    private val players: HashMap<UUID, TeamType> = hashMapOf()

    private val plannedTeams = TeamType.entries.associateWith { PlannedTeam(null, null) }

    var center: Location? = Location(Bukkit.getWorld("world"), -29.73, 72.00, -32.11)

    var centerRadius: Int = 500

    fun setSpawn(teamType: TeamType, location: Location) {
        plannedTeams[teamType]?.spawn = location
    }

    fun setBeacon(teamType: TeamType, location: Location) {
        plannedTeams[teamType]?.beacon = location
    }

    private fun isNoOne() = players.isEmpty()

    fun setExpectTeam(uuid: UUID, teamType: TeamType) {
        players[uuid] = teamType
    }

    fun clearPlayers() {
        players.clear()
    }

    private fun buildTeam(teamType: TeamType): GameTeam {
        val spawn = plannedTeams[teamType]?.spawn ?: throw NullPointerException("§7「§7${teamType.display}§7」のスポーンが設定されていません")
        val beacon = plannedTeams[teamType]?.beacon ?: throw NullPointerException("§7「§7${teamType.display}§7」のビーコンの位置が設定されていません")

        return GameTeam(spawn, beacon, teamType)
    }

    fun build(): SurvivalGame {
        if (isNoOne()) throw ArrayIndexOutOfBoundsException("このゲームには参加者がいません")
        val center = center ?: throw NullPointerException("ボーダーの中心が設定されていません")

        val teams = TeamType.entries.associateWith { buildTeam(it) }

        players.forEach { (uuid, teamType) ->
            val team = teams.getValue(teamType)
            team.registerTeammate(Bukkit.getPlayer(uuid) ?: return@forEach)
        }

        return SurvivalGame(teams.values.toList(), center, centerRadius)
    }

    private data class PlannedTeam(
        var spawn: Location?,
        var beacon: Location?,
    )
}