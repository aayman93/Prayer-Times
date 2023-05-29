package com.github.aayman93.prayertimes.data.models

import com.github.aayman93.prayertimes.data.source.remote.models.RemotePrayerTimes

fun RemotePrayerTimes.toExternalModel(): PrayerTimes {
    return PrayerTimes(
        fajr = this.timings.Fajr,
        sunrise = this.timings.Sunrise,
        dhuhr = this.timings.Dhuhr,
        asr = this.timings.Asr,
        maghrib = this.timings.Maghrib,
        isha = this.timings.Isha,
        date = this.date.readable,
        day = this.date.gregorian.day.toInt(),
        latitude = this.meta.latitude,
        longitude = this.meta.longitude
    )
}