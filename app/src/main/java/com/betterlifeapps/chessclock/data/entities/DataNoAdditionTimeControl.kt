package com.betterlifeapps.chessclock.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "no_addition_time_controls")
class DataNoAdditionTimeControl(
    @PrimaryKey
    val id: Int,
    val totalTime: Long,
)