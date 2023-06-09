package com.github.aayman93.prayertimes.data.repositories

import com.github.aayman93.prayertimes.data.models.PrayersInfo
import com.github.aayman93.prayertimes.data.models.toExternalModel
import com.github.aayman93.prayertimes.data.models.toLocalModel
import com.github.aayman93.prayertimes.data.source.local.PrayersDao
import com.github.aayman93.prayertimes.data.source.local.entites.LocalPrayersInfo
import com.github.aayman93.prayertimes.data.source.remote.PrayersApi
import com.github.aayman93.prayertimes.data.source.remote.models.prayer_times.RemotePrayersInfo
import com.github.aayman93.prayertimes.data.source.remote.models.qibla.Qibla
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

class DefaultPrayersRepository @Inject constructor(
    private val api: PrayersApi,
    private val dao: PrayersDao
) : PrayersRepository {

    override suspend fun getTodayPrayerTimes(
        year: Int,
        month: Int,
        day: Int,
        latitude: Double,
        longitude: Double
    ): PrayersInfo? {
        return withContext(Dispatchers.IO) {
            val remotePrayersInfoList = getPrayersTimes(year, month, latitude, longitude)
            if (remotePrayersInfoList.isNotEmpty()) {
                clearData()
                insertData(remotePrayersInfoList.map { it.toLocalModel() })
            }
            getPrayersInfoByDate(day)
        }
    }

    private suspend fun getPrayersTimes(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double
    ): List<RemotePrayersInfo> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val response = api.getPrayerTimes(year, month, latitude, longitude)
                if (response.code == 200 && response.data.isNotEmpty()) {
                    response.data
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getPrayersInfoByDate(dayDate: Int): PrayersInfo? {
        return withContext(Dispatchers.IO) {
            dao.getPrayersInfoByDate(dayDate).toExternalModel()
        }
    }

    override suspend fun getQiblaDirections(
        latitude: Double,
        longitude: Double
    ): Qibla? {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val response = api.getQiblaDirections(latitude, longitude)
                if (response.code == 0) {
                    response.data
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun clearData() {
        return withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }

    private suspend fun insertData(prayersInfoList: List<LocalPrayersInfo>) {
        return withContext(Dispatchers.IO) {
            dao.insertAll(prayersInfoList)
        }
    }
}