package com.github.aayman93.prayertimes.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.aayman93.prayertimes.data.source.local.entites.LocalPrayersInfo

@Database(entities = [LocalPrayersInfo::class], version = 1)
abstract class PrayersDatabase : RoomDatabase() {
    abstract fun prayersDao(): PrayersDao
}