package com.github.aayman93.prayertimes.data.repositories

import com.github.aayman93.prayertimes.data.models.PrayersInfo
import com.github.aayman93.prayertimes.data.source.remote.models.qibla.Qibla

interface PrayersRepository {

    suspend fun getTodayPrayerTimes(
        year: Int,
        month: Int,
        day: Int,
        latitude: Double,
        longitude: Double
    ): PrayersInfo?

    suspend fun getPrayersInfoByDate(dayDate: Int): PrayersInfo?

    suspend fun getQiblaDirections(
        latitude: Double,
        longitude: Double
    ): Qibla?
}