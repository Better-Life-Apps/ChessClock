package com.betterlifeapps.chessclock.data

import com.betterlifeapps.chessclock.data.entities.DataAdditionTimeControl
import com.betterlifeapps.chessclock.data.entities.DataConstantTimeControl
import com.betterlifeapps.chessclock.data.entities.DataNoAdditionTimeControl
import com.betterlifeapps.chessclock.domain.TimeControl
import javax.inject.Inject

interface TimeControlRepository {
    suspend fun saveTimeControl(timeControl: TimeControl): Int
    suspend fun getTimeControlById(id: Int): TimeControl
}

class TimeControlRepositoryImpl @Inject constructor(private val appDatabase: AppDatabase) :
    TimeControlRepository {

    override suspend fun saveTimeControl(timeControl: TimeControl): Int {
        val id = System.nanoTime().toInt()
        when (timeControl) {
            is TimeControl.ConstantTimeControl -> {
                val dataTimeControl = DataConstantTimeControl(id, timeControl.timePerTurn)
                appDatabase.constantTimeControlDao.insertTimeControl(dataTimeControl)
            }
            is TimeControl.AdditionTimeControl -> {
                val dataTimeControl =
                    DataAdditionTimeControl(id, timeControl.startTime, timeControl.additionTime)
                appDatabase.additionTimeControlDao.insertTimeControl(dataTimeControl)
            }
            is TimeControl.NoAdditionTimeControl -> {
                val dataTimeControl = DataNoAdditionTimeControl(id, timeControl.totalTime)
                appDatabase.noAdditionTimeControlDao.insertTimeControl(dataTimeControl)
            }
        }
        return id
    }

    override suspend fun getTimeControlById(id: Int): TimeControl {
        appDatabase.constantTimeControlDao.getTimeControlById(id)?.also {
            return TimeControl.ConstantTimeControl(it.timePerTurn)
        }
        appDatabase.additionTimeControlDao.getTimeControlById(id)?.also {
            return TimeControl.AdditionTimeControl(it.startTime, it.timeAddition)
        }
        appDatabase.noAdditionTimeControlDao.getTimeControlById(id)?.also {
            return TimeControl.NoAdditionTimeControl(it.totalTime)
        }
        throw IllegalArgumentException("Can't find time control with id:${id}")
    }
}