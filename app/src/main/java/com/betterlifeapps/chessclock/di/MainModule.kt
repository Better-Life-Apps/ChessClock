package com.betterlifeapps.chessclock.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.betterlifeapps.chessclock.common.ResourceResolverImpl
import com.betterlifeapps.chessclock.data.AppDatabase
import com.betterlifeapps.chessclock.data.GameModeRepository
import com.betterlifeapps.chessclock.data.GameModeRepositoryImpl
import com.betterlifeapps.chessclock.data.TimeControlRepository
import com.betterlifeapps.chessclock.data.TimeControlRepositoryImpl
import com.betterlifeapps.std.ResourceResolver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {

    companion object {
        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
            var database: AppDatabase? = null

            val callback = object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.Default).launch {
                        database?.addStandardGameModes(context)
                    }
                }
            }
            database =
                Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
                    .addCallback(callback)
                    .build()
            return database
        }
    }

    @Singleton
    @Binds
    abstract fun bindGameModeRepository(gameModeRepository: GameModeRepositoryImpl): GameModeRepository

    @Singleton
    @Binds
    abstract fun bindTimeControlRepository(timeControlRepositoryImpl: TimeControlRepositoryImpl): TimeControlRepository

    @Singleton
    @Binds
    abstract fun bindResourceResolver(resourceResolverImpl: ResourceResolverImpl): ResourceResolver
}