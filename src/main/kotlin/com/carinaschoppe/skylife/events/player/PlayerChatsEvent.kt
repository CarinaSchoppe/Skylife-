package com.carinaschoppe.skylife.events.player

import com.carinaschoppe.skylife.game.management.GameCluster
import com.carinaschoppe.skylife.game.management.gamestates.EndState
import com.carinaschoppe.skylife.game.management.gamestates.IngameState
import com.carinaschoppe.skylife.game.management.gamestates.LobbyState
import com.carinaschoppe.skylife.utility.messages.Messages
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerChatsEvent : Listener {


    @EventHandler(ignoreCancelled = true)
    fun onAsyncChat(event: AsyncChatEvent) {

        //check if player is in a game
        val game = GameCluster.lobbyGames.firstOrNull { it.livingPlayers.contains(event.player) or it.spectators.contains(event.player) } ?: GameCluster.activeGames.firstOrNull { it.livingPlayers.contains(event.player) or it.spectators.contains(event.player) } ?: run {
            event.player.sendMessage(Messages.instance.ALLREADY_IN_GAME)
            return
        }
        event.isCancelled = true

        val message: Component
        if (game.currentState is LobbyState) {
            message = Component.text("[LOBBY] ", NamedTextColor.GRAY).append(Component.text("${event.player.name}", NamedTextColor.WHITE).append(event.message()))
            game.livingPlayers.forEach { it.sendMessage(message) }
            game.spectators.forEach { it.sendMessage(message) }
        } else if (game.currentState is IngameState) {
            if (game.livingPlayers.contains(event.player)) {
                message = Component.text("[INGAME] ", NamedTextColor.GRAY).append(Component.text("${event.player.name}", NamedTextColor.WHITE).append(event.message()))
                game.livingPlayers.forEach { it.sendMessage(message) }
                game.spectators.forEach { it.sendMessage(message) }
            } else {
                message = Component.text("[SPECTATOR] ", NamedTextColor.GRAY).append(Component.text("${event.player.name}", NamedTextColor.WHITE).append(event.message()))
                game.spectators.forEach { it.sendMessage(message) }
            }
        } else if (game.currentState is EndState) {
            message = Component.text("[END] ", NamedTextColor.GRAY).append(Component.text("${event.player.name}", NamedTextColor.WHITE).append(event.message()))
            game.livingPlayers.forEach { it.sendMessage(message) }
            game.spectators.forEach { it.sendMessage(message) }
        }
    }
}