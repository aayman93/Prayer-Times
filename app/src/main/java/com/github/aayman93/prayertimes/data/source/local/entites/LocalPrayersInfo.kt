package com.github.aayman93.prayertimes.data.source.local.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayers")
data class LocalPrayersInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
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
