package com.betterlifeapps.chessclock.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.betterlifeapps.chessclock.domain.PlayerState
import com.betterlifeapps.chessclock.domain.State

@Entity(tableName = "game_state")
data class DataGameState(
    @PrimaryKey
    val id: Int = 1,
    @Embedded(prefix = "player1_")
    val player1: PlayerState,
    @Embedded(prefix = "player2_")
    val player2: PlayerState,
    val isFirstPlayerTurn: Boolean,
    val state: State
)