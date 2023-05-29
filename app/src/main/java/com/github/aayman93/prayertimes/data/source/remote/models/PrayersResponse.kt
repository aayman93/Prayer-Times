package com.github.aayman93.prayertimes.data.source.remote.models

data class PrayersResponse(
    val code: Int,
    val `data`: List<RemotePrayerTimes>,
    val status: String
)