package com.github.aayman93.prayertimes.data.repositories

import com.github.aayman93.prayertimes.data.models.PrayersInfo

interface PrayersRepository {

    suspend fun getTodayPrayerTimes(
        year: Int,
        month: Int,
        day: Int,
        latitude: Double,
        longitude: Double
    ): PrayersInfo?

    suspend fun getPrayersInfoByDate(dayDate: Int): PrayersInfo?
}