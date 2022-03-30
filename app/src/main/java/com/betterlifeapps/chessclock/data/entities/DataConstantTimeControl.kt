package com.betterlifeapps.chessclock.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "constant_time_controls")
class DataConstantTimeControl(
    @PrimaryKey
    val id: Int,
    val timePerTurn: Long,
)