package com.dpott197.weather.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.dpott197.weather.R
import com.dpott197.weather.adapter.ForecastAdapter
import com.dpott197.weather.model.WeatherList
import com.dpott197.weather.mvvm.WeatherViewModel
import com.dpott197.weather.service.LocationHelper
import com.dpott197.weather.util.SharedPrefs
import com.dpott197.weather.util.Utils

class ForecastActivity : AppCompatActivity() {

    private lateinit var forecastAdapter: ForecastAdapter
    lateinit var weatherViewModel : WeatherViewModel
    lateinit var forecastRecyclerView: RecyclerView

    private lateinit var locationHelper: LocationHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fourdayforecast)

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        locationHelper = LocationHelper(this)
        forecastAdapter = ForecastAdapter()
        forecastRecyclerView = findViewById<RecyclerView>(R.id.rvForeCast)

        val sharedPrefs = SharedPrefs.getInstance(this)
        val city = sharedPrefs.getValueOrNull("city")

        if (city != null){
            weatherViewModel.getForecastUpcoming(city)
        } else {
            if (locationHelper.isLocationPermissionGranted()) {
                // Permission is granted, request location updates
                requestLocationUpdates()
            } else {
                // Request location permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Utils.LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }

        weatherViewModel.forecastWeatherLiveData.observe(this, Observer {
            val setNewlist = it as List<WeatherList>
            forecastAdapter.setList(setNewlist)
            forecastRecyclerView.adapter = forecastAdapter
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestLocationUpdates() {
        locationHelper.requestLocationUpdates { location ->
            // Log latitude and longitude here
            val latitude = location.latitude
            val longitude = location.longitude

            weatherViewModel.getForecastUpcoming(null, latitude.toString(), longitude.toString())
            logLocation(latitude, longitude)
        }
    }

    private fun logLocation(latitude: Double, longitude: Double) {
        // Log the latitude and longitude
        val message = "Lat: $latitude, Long: $longitude"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Utils.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(
                    this,
                    R.string.location_permission_denied,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}