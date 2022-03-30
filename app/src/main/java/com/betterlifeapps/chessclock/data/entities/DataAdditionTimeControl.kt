package com.betterlifeapps.chessclock.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addition_time_controls")
class DataAdditionTimeControl(
    @PrimaryKey
    val id: Int,
    val startTime: Long,
    val timeAddition: Long
)