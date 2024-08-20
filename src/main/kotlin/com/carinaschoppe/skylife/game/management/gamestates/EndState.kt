package com.carinaschoppe.skylife.game.management.gamestates

import com.carinaschoppe.skylife.game.management.Game
import com.carinaschoppe.skylife.game.management.countdown.Countdown
import com.carinaschoppe.skylife.game.management.countdown.EndCountdown
import com.carinaschoppe.skylife.game.miscellaneous.Utility

class EndState(game: Game) : GameState(game) {

    override val gameStateID: Int = GameStates.END_STATE.id
    override val countdown: Countdown = EndCountdown(game)

    override fun start() {
        game.currentState = this
        //todo: broadcast message to all players
        countdown.start()
        Utility.endingMatchMessage(game)
    }


    override fun stop() {
        game.cancel()

    }


}