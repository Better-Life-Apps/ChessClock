package com.betterlifeapps.chessclock.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.data.dao.AdditionTimeControlDao
import com.betterlifeapps.chessclock.data.dao.ConstantTimeControlDao
import com.betterlifeapps.chessclock.data.dao.GameModeDao
import com.betterlifeapps.chessclock.data.dao.GameStateDao
import com.betterlifeapps.chessclock.data.dao.NoAdditionTimeControlDao
import com.betterlifeapps.chessclock.data.entities.DataAdditionTimeControl
import com.betterlifeapps.chessclock.data.entities.DataConstantTimeControl
import com.betterlifeapps.chessclock.data.entities.DataGameMode
import com.betterlifeapps.chessclock.data.entities.DataGameState
import com.betterlifeapps.chessclock.data.entities.DataNoAdditionTimeControl
import com.betterlifeapps.chessclock.data.typeconverters.OffsetDateTimeTypeConverter
import java.time.OffsetDateTime

private const val DATABASE_VERSION = 2

@Database(
    entities = [DataGameMode::class, DataConstantTimeControl::class, DataAdditionTimeControl::class, DataNoAdditionTimeControl::class, DataGameState::class],
    version = DATABASE_VERSION,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(OffsetDateTimeTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val constantTimeControlDao: ConstantTimeControlDao
    abstract val additionTimeControlDao: AdditionTimeControlDao
    abstract val noAdditionTimeControlDao: NoAdditionTimeControlDao
    abstract val gameModeDao: GameModeDao
    abstract val gameStateDao: GameStateDao

    suspend fun addStandardGameModes(context: Context) = withTransaction {
        addRapidGameMode(context)
        addBlitzGameMode(context)
        addBulletGameMode(context)
    }

    private suspend fun addRapidGameMode(context: Context) {
        val rapidTimeControl = DataAdditionTimeControl(1, 10 * 60 * 1000, 10_000)
        additionTimeControlDao.insertTimeControl(rapidTimeControl)
        val rapidGameMode = DataGameMode(
            1,
            true,
            context.getString(R.string.rapid),
            OffsetDateTime.now(),
            true,
            1,
            1
        )
        gameModeDao.insertGameMode(rapidGameMode)
    }

    private suspend fun addBlitzGameMode(context: Context) {
        val blitzTimeControl = DataAdditionTimeControl(2, 3 * 60 * 1000, 2_000)
        additionTimeControlDao.insertTimeControl(blitzTimeControl)
        val blitzGameMode = DataGameMode(
            2,
            false,
            context.getString(R.string.blitz),
            OffsetDateTime.now(),
            true,
            2,
            2
        )
        gameModeDao.insertGameMode(blitzGameMode)
    }

    private suspend fun addBulletGameMode(context: Context) {
        val bulletTimeControl = DataAdditionTimeControl(3, 60 * 1000, 1_000)
        additionTimeControlDao.insertTimeControl(bulletTimeControl)
        val bulletGameMode = DataGameMode(
            3,
            false,
            context.getString(R.string.bullet),
            OffsetDateTime.now(),
            true,
            3,
            3
        )
        gameModeDao.insertGameMode(bulletGameMode)
    }

    companion object {
        const val DATABASE_NAME = "chess_clock_database"
    }
}