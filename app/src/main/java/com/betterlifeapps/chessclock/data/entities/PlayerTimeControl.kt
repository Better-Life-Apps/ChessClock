package com.betterlifeapps.chessclock.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "constant_time_modes")
class DataConstantTimeControl(
    @PrimaryKey
    val id: Int,
    val timePerTurn: Long,
)

@Entity(tableName = "addition_time_modes")
class DataAdditionTimeControl(
    @PrimaryKey
    val id: Int,
    val startTime: Long,
    val timeAddition: Long
)

@Entity(tableName = "no_addition_time_modes")
class DataNoAdditionTimeControl(
    @PrimaryKey
    val id: Int,
    val totalTime: Long,
)