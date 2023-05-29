package com.github.aayman93.prayertimes.data.repositories

import com.github.aayman93.prayertimes.data.models.PrayerTimes
import com.github.aayman93.prayertimes.data.models.toExternalModel
import com.github.aayman93.prayertimes.data.source.remote.PrayersApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultPrayersRepository @Inject constructor(
    private val api: PrayersApi
) : PrayersRepository {

    override suspend fun getPrayerTimes(): List<PrayerTimes> {
        return withContext(Dispatchers.IO) {
            api.getPrayerTimes().data.map { it.toExternalModel() }
        }
    }
}