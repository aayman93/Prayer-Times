package com.github.aayman93.prayertimes.data.source.remote.models.prayer_times

data class PrayersResponse(
    val code: Int,
    val `data`: List<RemotePrayersInfo>,
    val status: String
)