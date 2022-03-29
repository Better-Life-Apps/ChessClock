package com.betterlifeapps.chessclock.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.betterlifeapps.chessclock.data.entities.DataAdditionTimeControl

@Dao
interface AdditionTimeControlDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertTimeControl(timeControl: DataAdditionTimeControl)

    @Query("SELECT * FROM `addition_time_modes` WHERE id = :id")
    suspend fun getTimeControlById(id: Int): DataAdditionTimeControl?
}