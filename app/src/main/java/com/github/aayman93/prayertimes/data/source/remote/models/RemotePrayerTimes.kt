package com.github.aayman93.prayertimes.data.source.remote.models

data class RemotePrayerTimes(
    val date: Date,
    val meta: Meta,
    val timings: Timings
)