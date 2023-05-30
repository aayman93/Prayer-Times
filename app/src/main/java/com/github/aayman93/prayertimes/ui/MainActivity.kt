package com.github.aayman93.prayertimes.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.aayman93.prayertimes.R
import com.github.aayman93.prayertimes.data.models.PrayersInfo
import com.github.aayman93.prayertimes.databinding.ActivityMainBinding
import com.github.aayman93.prayertimes.util.convertTimeFormat
import com.github.aayman93.prayertimes.util.parseTime
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private var isCountdownEnabled: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPrayerTimes()

        setupObservables()
        setupListeners()
    }

    @SuppressLint("MissingPermission")
    private fun getPrayerTimes() {
        if (hasLocationPermissions()) {
            if (isLocationEnabled()) {
                val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task ->
                    val location = task.result
                    location?.let {
                        val calendar = Calendar.getInstance()
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH) + 1 // For some reason this method get the previous month!
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        viewModel.getPrayerTimes(year, month, day, it.latitude, it.longitude)
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_CODE
        )
    }

    private fun setupListeners() {
        binding.buttonNext.setOnClickListener {
            viewModel.getNextDayPrayerTimes()
        }

        binding.buttonPrevious.setOnClickListener {
            viewModel.getPreviousDayPrayerTimes()
        }

        binding.buttonTryAgain.setOnClickListener {
            getPrayerTimes()
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
                setNextPrayer(prayersInfo)
                Thread {
                    while (isCountdownEnabled) {
                        Thread.sleep(60000)
                        runOnUiThread { calculateTimeTillNextPrayer(prayersInfo) }
                    }
                }.start()
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

    private fun setNextPrayer(prayersInfo: PrayersInfo) {
        val calendar = Calendar.getInstance()
        if (prayersInfo.day != calendar.get(Calendar.DAY_OF_MONTH)) return

        calculateTimeTillNextPrayer(prayersInfo)
    }

    private fun calculateTimeTillNextPrayer(prayersInfo: PrayersInfo) {
        var timeDifference: Long = 0
        var nextPrayer = ""

        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis

        if (calculateTimeDifference(prayersInfo.isha, now) > 0) {

            val differenceList = listOf(
                calculateTimeDifference(prayersInfo.fajr, now),
                calculateTimeDifference(prayersInfo.dhuhr, now),
                calculateTimeDifference(prayersInfo.asr, now),
                calculateTimeDifference(prayersInfo.maghrib, now),
                calculateTimeDifference(prayersInfo.isha, now)
            )

            val prayersList = listOf(getString(R.string.fajr), getString(R.string.dhuhr),
                getString(R.string.asr), getString(R.string.maghrib), getString(R.string.isha))


            for (dif in differenceList) {
                if (dif > 0) {
                    if (timeDifference <= 0 || dif < timeDifference) {
                        timeDifference = dif
                        nextPrayer = prayersList[differenceList.indexOf(dif)]
                    }
                }
            }
        } else {
            timeDifference = calculateTimeDifference(prayersInfo.fajr, now, true)
            nextPrayer = getString(R.string.fajr)
        }


        val minutes = (timeDifference / (1000 * 60) % 60)
        val hours = (timeDifference / (1000 * 60 * 60) % 24)

        binding.textNextPrayer.text = nextPrayer
        binding.textTimeLeft.text = getString(R.string.time_remaining, hours, minutes)
    }

    private fun calculateTimeDifference(time: String, now: Long, isNextDay: Boolean = false) : Long {
        val timeParts = time.parseTime().split(":")
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, timeParts[0].toInt())
        calendar.set(java.util.Calendar.MINUTE, timeParts[1].toInt())
        if (isNextDay) calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)

        val difference = calendar.timeInMillis - now
        return if (difference < 0 ) 0 else difference
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_CODE && grantResults.isNotEmpty()) {
            getPrayerTimes()
        }
    }

    override fun onDestroy() {
        isCountdownEnabled = false
        super.onDestroy()
    }

    companion object {
        private const val LOCATION_PERMISSION_CODE = 100
    }
}