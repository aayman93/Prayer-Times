package com.github.aayman93.prayertimes.data.repositories

import com.github.aayman93.prayertimes.data.models.PrayerTimes

interface PrayersRepository {

    suspend fun getPrayerTimes(): List<PrayerTimes>
}