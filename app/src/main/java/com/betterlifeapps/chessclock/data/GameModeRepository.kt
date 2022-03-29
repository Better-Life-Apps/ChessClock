package com.betterlifeapps.chessclock.data


import androidx.room.withTransaction
import com.betterlifeapps.chessclock.data.entities.DataGameMode
import com.betterlifeapps.chessclock.domain.GameMode
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GameModeRepository {
    suspend fun saveGameMode(gameMode: GameMode)
    fun getCustomGameModes(): Flow<List<GameMode>>
    fun getStandardGameModes(): Flow<List<GameMode>>
}

@Singleton
class GameModeRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val timeControlRepository: TimeControlRepository
) : GameModeRepository {

    override fun getCustomGameModes(): Flow<List<GameMode>> {
        return appDatabase.gameModeDao.getCustomGameModes()
            .map {
                it.map {
                    val player1Control =
                        timeControlRepository.getTimeControlById(it.player1TimeControlId)
                    val player2Control =
                        timeControlRepository.getTimeControlById(it.player2TimeControlId)
                    GameMode(
                        it.id,
                        it.isStandard,
                        it.name,
                        it.isSelected,
                        it.creationDate,
                        player1Control,
                        player2Control
                    )
                }
            }
    }

    override fun getStandardGameModes(): Flow<List<GameMode>> {
        return appDatabase.gameModeDao.getStandardGameModes()
            .map {
                it.map {
                    val player1Control =
                        timeControlRepository.getTimeControlById(it.player1TimeControlId)
                    val player2Control =
                        timeControlRepository.getTimeControlById(it.player2TimeControlId)
                    GameMode(
                        it.id,
                        it.isStandard,
                        it.name,
                        it.isSelected,
                        it.creationDate,
                        player1Control,
                        player2Control
                    )
                }
            }
    }

    override suspend fun saveGameMode(gameMode: GameMode) {
        appDatabase.withTransaction {
            val player1Control = gameMode.player1TimeControl
            val player1ControlId = timeControlRepository.saveTimeControl(player1Control)
            val player2Control = gameMode.player2TimeControl
            val player2ControlId = timeControlRepository.saveTimeControl(player2Control)
            val dataGameMode = DataGameMode(
                0,
                gameMode.isSelected,
                gameMode.name,
                gameMode.createDate,
                gameMode.isStandard,
                player1ControlId,
                player2ControlId
            )
            appDatabase.gameModeDao.insertGameMode(dataGameMode)
        }
    }
}