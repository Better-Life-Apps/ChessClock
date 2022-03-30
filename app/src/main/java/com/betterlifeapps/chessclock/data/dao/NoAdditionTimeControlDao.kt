package com.betterlifeapps.chessclock.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.betterlifeapps.chessclock.data.entities.DataNoAdditionTimeControl

@Dao
interface NoAdditionTimeControlDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertTimeControl(timeControl: DataNoAdditionTimeControl)

    @Query("SELECT * FROM `no_addition_time_controls` WHERE id = :id")
    suspend fun getTimeControlById(id: Int): DataNoAdditionTimeControl?
}