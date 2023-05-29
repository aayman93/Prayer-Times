package com.github.aayman93.prayertimes.data.source.remote

import com.github.aayman93.prayertimes.data.source.remote.models.PrayersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PrayersApi {

    @GET("v1/calendar/{year}/{month}")
    suspend fun getPrayerTimes(
        @Path("year") year: Int = 2023,
        @Path("month") month: Int = 5,
        @Query("latitude") latitude: Double = 31.037933,
        @Query("longitude") longitude: Double = 31.381523,
        @Query("method") method: Int = 5
    ): PrayersResponse
}