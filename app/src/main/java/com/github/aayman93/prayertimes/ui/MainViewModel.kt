package com.github.aayman93.prayertimes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aayman93.prayertimes.data.models.PrayerTimes
import com.github.aayman93.prayertimes.data.repositories.PrayersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PrayersRepository
) : ViewModel() {

    private val _prayerTimes = MutableLiveData<List<PrayerTimes>>()
    val prayerTimes: LiveData<List<PrayerTimes>> = _prayerTimes

    fun getPrayerTimes() {
        viewModelScope.launch(Dispatchers.Main) {
            val prayerTimesList = repository.getPrayerTimes()
            _prayerTimes.postValue(prayerTimesList)
        }
    }
}