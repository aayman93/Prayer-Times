package com.github.aayman93.prayertimes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aayman93.prayertimes.data.models.PrayersInfo
import com.github.aayman93.prayertimes.data.repositories.PrayersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PrayersRepository
) : ViewModel() {

    private var todayDate: Int = 0
    private var currentlyShownDayDate: Int = 0

    var userLatitude: Double = 0.0
    var userLongitude: Double = 0.0


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _prayersInfo = MutableLiveData<PrayersInfo?>()
    val prayersInfo: LiveData<PrayersInfo?> = _prayersInfo

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getPrayerTimes(
        year: Int,
        month: Int,
        day: Int,
        latitude: Double,
        longitude: Double
    ) {
        todayDate = day
        currentlyShownDayDate = day
        userLatitude = latitude
        userLongitude = longitude
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main) {
            val prayerTimes = repository.getTodayPrayerTimes(year, month, day, latitude, longitude)
            _isLoading.postValue(false)
            prayerTimes?.let {
                _prayersInfo.postValue(it)
            } ?: _error.postValue("Error getting prayer times data")
        }
    }

    fun getNextDayPrayerTimes() {
        if (currentlyShownDayDate - todayDate == 6 || currentlyShownDayDate > 29) return
        currentlyShownDayDate++
        getPrayersInfoByDate(currentlyShownDayDate)
    }

    fun getPreviousDayPrayerTimes() {
        if (currentlyShownDayDate == todayDate) return
        currentlyShownDayDate--
        getPrayersInfoByDate(currentlyShownDayDate)
    }

    private fun getPrayersInfoByDate(day: Int) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main) {
            val prayerTimes = repository.getPrayersInfoByDate(day)
            _isLoading.postValue(false)
            prayerTimes?.let {
                _prayersInfo.postValue(it)
            } ?: _error.postValue("Error getting prayer times data")
        }
    }
}