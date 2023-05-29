package com.github.aayman93.prayertimes.ui

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
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

        viewModel.getPrayerTimes(2023, 5, 20, 31.037933, 31.381523)

        setupObservables()
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonNext.setOnClickListener {
            viewModel.getNextDayPrayerTimes()
        }

        binding.buttonPrevious.setOnClickListener {
            viewModel.getPreviousDayPrayerTimes()
        }

        binding.buttonTryAgain.setOnClickListener {
            viewModel.getPrayerTimes(2023, 5, 20, 31.037933, 31.381523)
        }
    }

    @Suppress("DEPRECATION")
    private fun setupObservables() {
        viewModel.prayersInfo.observe(this) { prayersInfo ->
            prayersInfo?.let {
                binding.mainLayout.isVisible = true
                val address = Geocoder(this)
                    .getFromLocation(prayersInfo.latitude, prayersInfo.longitude, 1)?.get(0)
                binding.textAddress.text =
                    getString(R.string.address, address?.locality, address?.countryName)
                binding.fajrTime.text = prayersInfo.fajr.convertTimeFormat()
                binding.sunriseTime.text = prayersInfo.sunrise.convertTimeFormat()
                binding.dhuhrTime.text = prayersInfo.dhuhr.convertTimeFormat()
                binding.asrTime.text = prayersInfo.asr.convertTimeFormat()
                binding.maghribTime.text = prayersInfo.maghrib.convertTimeFormat()
                binding.ishaTime.text = prayersInfo.isha.convertTimeFormat()
                binding.textDate.text = prayersInfo.date
                binding.textNextPrayer.text = getString(R.string.isha)
                binding.textTimeLeft.text = getString(R.string.time_remaining, 1, 25)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.isVisible = true
                binding.mainLayout.isVisible = false
                binding.errorLayout.isVisible = false
            } else {
                binding.progressBar.isVisible = false
            }
        }

        viewModel.error.observe(this) { error ->
            if (!error.isNullOrBlank()) {
                binding.errorText.text = error
                binding.errorLayout.isVisible = true
            }
        }
    }
}