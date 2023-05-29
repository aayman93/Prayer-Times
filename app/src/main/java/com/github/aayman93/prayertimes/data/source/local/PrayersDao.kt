package com.github.aayman93.prayertimes.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.aayman93.prayertimes.data.source.local.entites.LocalPrayersInfo

@Dao
interface PrayersDao {

    @Query("SELECT * FROM prayers WHERE day = :dayDate LIMIT 1")
    suspend fun getPrayersInfoByDate(dayDate: Int): LocalPrayersInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(prayersInfoList: List<LocalPrayersInfo>)

    @Query("DELETE FROM prayers")
    suspend fun deleteAll()
}