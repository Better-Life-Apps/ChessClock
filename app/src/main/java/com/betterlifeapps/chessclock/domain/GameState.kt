package com.betterlifeapps.chessclock.domain

data class GameState(val player1: PlayerState, val player2: PlayerState, val isFirstPlayerTurn: Boolean, val isPaused: Boolean, val isStarted: Boolean)
