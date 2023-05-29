package com.github.aayman93.prayertimes.data.models

data class PrayerTimes(
    val asr: String,
    val dhuhr: String,
    val fajr: String,
    val isha: String,
    val maghrib: String,
    val sunrise: String,
    val date: String,
    val day: Int,
    val latitude: Double,
    val longitude: Double
)
