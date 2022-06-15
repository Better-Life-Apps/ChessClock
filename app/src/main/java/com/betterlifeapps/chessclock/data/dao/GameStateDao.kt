package com.betterlifeapps.chessclock.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.betterlifeapps.chessclock.data.entities.DataGameState

@Dao
interface GameStateDao {

    @Query("SELECT * FROM game_state LIMIT 1")
    suspend fun getGameState(): DataGameState?

    @Insert(onConflict = REPLACE)
    suspend fun saveGameState(dataGameState: DataGameState?)

    @Query("DELETE FROM game_state")
    suspend fun deleteGameState()
}