package com.betterlifeapps.chessclock.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.betterlifeapps.chessclock.data.entities.DataConstantTimeControl

@Dao
interface ConstantTimeControlDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertTimeControl(timeControl: DataConstantTimeControl)

    @Query("SELECT * FROM `constant_time_controls` WHERE id = :id")
    suspend fun getTimeControlById(id: Int): DataConstantTimeControl?
}