package com.betterlifeapps.chessclock.data

import com.betterlifeapps.chessclock.data.entities.DataGameState
import com.betterlifeapps.chessclock.domain.GameState
import javax.inject.Inject

interface GameStateRepository {
    suspend fun getGameState(): GameState?
    suspend fun saveGameState(gameState: GameState)
}

class GameStateRepositoryImpl @Inject constructor(val appDatabase: AppDatabase) :
    GameStateRepository {

    override suspend fun getGameState(): GameState? {
        val dataGameState = appDatabase.gameStateDao.getGameState()
        return dataGameState?.let {
            GameState(
                it.player1,
                it.player2,
                it.isFirstPlayerTurn,
                it.state
            )
        }
    }

    override suspend fun saveGameState(gameState: GameState) {
        val dataGameState = DataGameState(
            id = 1,
            gameState.player1,
            gameState.player2,
            gameState.isFirstPlayerTurn,
            gameState.state
        )

        appDatabase.gameStateDao.saveGameState(dataGameState)
    }
}