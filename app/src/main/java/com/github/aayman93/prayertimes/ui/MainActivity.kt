package com.github.aayman93.prayertimes.ui

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.github.aayman93.prayertimes.R
import com.github.aayman93.prayertimes.databinding.ActivityMainBinding
import com.github.aayman93.prayertimes.util.convertTimeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getPrayerTimes()

        setupObservables()
    }

    @Suppress("DEPRECATION")
    private fun setupObservables() {
        viewModel.prayerTimes.observe(this) {
            val prayerTimes = it[0]
            val address = Geocoder(this)
                .getFromLocation(prayerTimes.latitude, prayerTimes.longitude, 1)?.get(0)
            binding.textAddress.text =
                getString(R.string.address, address?.locality, address?.countryName)
            binding.fajrTime.text = prayerTimes.fajr.convertTimeFormat()
            binding.sunriseTime.text = prayerTimes.sunrise.convertTimeFormat()
            binding.dhuhrTime.text = prayerTimes.dhuhr.convertTimeFormat()
            binding.asrTime.text = prayerTimes.asr.convertTimeFormat()
            binding.maghribTime.text = prayerTimes.maghrib.convertTimeFormat()
            binding.ishaTime.text = prayerTimes.isha.convertTimeFormat()
            binding.textDate.text = prayerTimes.date
            binding.textNextPrayer.text = getString(R.string.isha)
            binding.textTimeLeft.text = getString(R.string.time_remaining, 1, 25)
        }
    }
}