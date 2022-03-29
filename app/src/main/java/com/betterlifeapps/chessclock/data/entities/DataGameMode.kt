package com.betterlifeapps.chessclock.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = "game_modes")
data class DataGameMode(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val isSelected: Boolean,
    val name: String,
    val creationDate: OffsetDateTime,
    val isStandard: Boolean,
    val player1TimeControlId: Int,
    val player2TimeControlId: Int
)