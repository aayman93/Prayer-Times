package com.github.aayman93.prayertimes.data.models

import com.github.aayman93.prayertimes.data.source.local.entites.LocalPrayersInfo
import com.github.aayman93.prayertimes.data.source.remote.models.prayer_times.RemotePrayersInfo

fun RemotePrayersInfo.toLocalModel(): LocalPrayersInfo {
    return LocalPrayersInfo(
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

fun LocalPrayersInfo?.toExternalModel(): PrayersInfo? {
    if (this == null) return null
    return PrayersInfo(
        fajr = this.fajr,
        sunrise = this.sunrise,
        dhuhr = this.dhuhr,
        asr = this.asr,
        maghrib = this.maghrib,
        isha = this.isha,
        date = this.date,
        day = this.day,
        latitude = this.latitude,
        longitude = this.longitude
    )
}