package com.carinaschoppe.skylife.utility.statistics

import com.carinaschoppe.skylife.game.management.GameCluster
import com.carinaschoppe.skylife.game.management.gamestates.IngameState
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

object StatsUtility {

    fun addStatsToPlayerWhenLeave(player: Player) {
        //get player from Database
        val statsPlayer = statsPlayers.first { it.uuid == player.uniqueId.toString() }
        if (GameCluster.activeGames.any { it.livingPlayers.contains(player) && it.currentState is IngameState }) {
            transaction {
                statsPlayer.deaths
            }
        }
    }


    fun addWinStatsToPlayer(player: Player) {
        val statsPlayer = statsPlayers.first { it.uuid == player.uniqueId.toString() }
        transaction {
            statsPlayer.wins++

        }
    }

    fun addDeathStatsToPlayer(player: Player) {
        val statsPlayer = statsPlayers.first { it.uuid == player.uniqueId.toString() }
        transaction {
            statsPlayer.deaths++

        }
    }

    fun addKillStatsToPlayer(player: Player) {
        val statsPlayer = statsPlayers.first { it.uuid == player.uniqueId.toString() }
        transaction {
            statsPlayer.kills++

        }
    }

    fun addStatsPlayerWhenFirstJoin(player: Player) {
        //add stats
        val statsPlayer: StatsPlayer = statsPlayers.firstOrNull { it.uuid == player.uniqueId.toString() } ?: StatsPlayer.new {
            uuid = player.uniqueId.toString()
            kills = 0
            name = player.name
            deaths = 0
            wins = 0
            games = 0
        }
        transaction {
            statsPlayers.add(statsPlayer)

        }

    }

    fun addStatsToPlayerWhenJoiningGame(player: Player) {
        val statsPlayer = statsPlayers.first { it.uuid == player.uniqueId.toString() }
        transaction {
            statsPlayer.games++
        }
    }

    val statsPlayers = mutableSetOf<StatsPlayer>()

}