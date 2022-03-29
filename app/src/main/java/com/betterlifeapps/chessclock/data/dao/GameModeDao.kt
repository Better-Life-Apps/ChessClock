package com.betterlifeapps.chessclock.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.betterlifeapps.chessclock.data.entities.DataGameMode
import kotlinx.coroutines.flow.Flow

@Dao
interface GameModeDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertGameMode(gameMode: DataGameMode)

    @Query("SELECT * FROM `game_modes` WHERE isStandard = 0")
    fun getCustomGameModes(): Flow<List<DataGameMode>>

    @Query("SELECT * FROM `game_modes` WHERE isStandard = 1")
    fun getStandardGameModes(): Flow<List<DataGameMode>>
}