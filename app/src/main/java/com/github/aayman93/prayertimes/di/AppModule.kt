package com.github.aayman93.prayertimes.di

import com.github.aayman93.prayertimes.data.repositories.DefaultPrayersRepository
import com.github.aayman93.prayertimes.data.repositories.PrayersRepository
import com.github.aayman93.prayertimes.data.source.local.PrayersDao
import com.github.aayman93.prayertimes.data.source.remote.PrayersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePrayersRepository(
        api: PrayersApi,
        dao: PrayersDao
    ): PrayersRepository {
        return DefaultPrayersRepository(api, dao)
    }
}