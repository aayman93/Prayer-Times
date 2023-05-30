package com.github.aayman93.prayertimes.data.source.remote.models.prayer_times

data class RemotePrayersInfo(
    val date: Date,
    val meta: Meta,
    val timings: Timings
)