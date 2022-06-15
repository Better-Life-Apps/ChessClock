package com.betterlifeapps.chessclock.data

import android.content.Context
import androidx.room.withTransaction
import com.betterlifeapps.chessclock.data.entities.DataGameMode
import com.betterlifeapps.chessclock.domain.GameMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

interface GameModeRepository {
    suspend fun saveGameMode(gameMode: GameMode)
    fun getCustomGameModes(): Flow<List<GameMode>>
    fun getStandardGameModes(): Flow<List<GameMode>>
    suspend fun selectGameMode(id: Int)
    fun getSelectedGameMode(): Flow<GameMode>
    suspend fun getSelectedGameModeSync(): GameMode?
    suspend fun deleteAndCheckSelection(id: Int, isSelected: Boolean)
}

@Singleton
class GameModeRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val timeControlRepository: TimeControlRepository,
    @ApplicationContext private val context: Context
) : GameModeRepository {

    override fun getCustomGameModes(): Flow<List<GameMode>> {
        return appDatabase.gameModeDao.getCustomGameModes()
            .map { list ->
                list.map { it.mapToGameMode() }
            }
    }

    override fun getStandardGameModes(): Flow<List<GameMode>> {
        return appDatabase.gameModeDao.getStandardGameModes()
            .map { list ->
                list.map { it.mapToGameMode() }
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

    override suspend fun selectGameMode(id: Int) {
        appDatabase.withTransaction {
            appDatabase.gameModeDao.deselectCurrentGameMode()
            appDatabase.gameModeDao.selectGameMode(id)
            // Clear game state because it's no longer valid and need to restart the game
            appDatabase.gameStateDao.deleteGameState()
        }
    }

    override fun getSelectedGameMode(): Flow<GameMode> {
        return appDatabase.gameModeDao.observeSelectedGameMode()
            .filterNotNull()
            .map {
                it.mapToGameMode()
            }
    }

    override suspend fun deleteAndCheckSelection(id: Int, isSelected: Boolean) {
        appDatabase.withTransaction {
            appDatabase.gameModeDao.deleteById(id)
            if (isSelected) {
                selectDefaultGameMode()
            }
        }
    }

    private suspend fun selectDefaultGameMode() {
        selectGameMode(0)
    }

    override suspend fun getSelectedGameModeSync(): GameMode? {
        return appDatabase.gameModeDao.getSelectedGameMode()?.mapToGameMode()
    }

    private suspend fun DataGameMode.mapToGameMode(): GameMode {
        val player1Control = timeControlRepository.getTimeControlById(player1TimeControlId)
        val player2Control = timeControlRepository.getTimeControlById(player2TimeControlId)
        return GameMode(
            id,
            isStandard,
            name,
            isSelected,
            creationDate,
            player1Control,
            player2Control
        )
    }
}