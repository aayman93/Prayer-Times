package com.github.aayman93.prayertimes.di

import android.content.Context
import androidx.room.Room
import com.github.aayman93.prayertimes.data.source.local.PrayersDao
import com.github.aayman93.prayertimes.data.source.local.PrayersDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePrayersDatabase(@ApplicationContext appContext: Context): PrayersDatabase {
        return Room.databaseBuilder(
            appContext,
            PrayersDatabase::class.java,
            "prayers.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePrayersDao(database: PrayersDatabase): PrayersDao {
        return database.prayersDao()
    }
}