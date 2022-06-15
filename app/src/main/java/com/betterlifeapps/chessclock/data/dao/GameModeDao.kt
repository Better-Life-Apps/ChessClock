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

    @Query("UPDATE game_modes SET isSelected = 0 WHERE isSelected = 1")
    fun deselectCurrentGameMode()

    @Query("UPDATE game_modes SET isSelected = 1 WHERE id = :id")
    fun selectGameMode(id: Int)

    @Query("SELECT * FROM game_modes WHERE isSelected = 1 LIMIT 1")
    fun observeSelectedGameMode(): Flow<DataGameMode?>

    @Query("SELECT * FROM game_modes WHERE isSelected = 1 LIMIT 1")
    suspend fun getSelectedGameMode(): DataGameMode?

    @Query("DELETE FROM game_modes WHERE id = :id")
    fun deleteById(id: Int)
}