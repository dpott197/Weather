package com.example.weatherapp.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.adapter.ForeCastAdapter
import com.example.weatherapp.model.WeatherList
import com.example.weatherapp.mvvm.WeatherViewModel
import com.example.weatherapp.service.LocationHelper
import com.example.weatherapp.util.SharedPrefs
import com.example.weatherapp.util.Utils

class ForecastActivity : AppCompatActivity() {
    companion object {
        private val LOG_TAG = ForecastActivity::class.qualifiedName
    }

    private lateinit var adapterForeCastAdapter: ForeCastAdapter
    lateinit var viM : WeatherViewModel
    lateinit var rvForeCast: RecyclerView

    private lateinit var locationHelper: LocationHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fourdayforecast)





        viM = ViewModelProvider(this).get(WeatherViewModel::class.java)

        locationHelper = LocationHelper(this)



        adapterForeCastAdapter = ForeCastAdapter()

        rvForeCast = findViewById<RecyclerView>(R.id.rvForeCast)


        val sharedPrefs = SharedPrefs.getInstance(this)
        val city = sharedPrefs.getValueOrNull("city")


        Log.d("Prefs", city.toString())



        if (city!=null){


            viM.getForecastUpcoming(city)

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




        viM.forecastWeatherLiveData.observe(this, Observer {
            val setNewlist = it as List<WeatherList>
            Log.d(LOG_TAG, setNewlist.toString())
            adapterForeCastAdapter.setList(setNewlist)
            rvForeCast.adapter = adapterForeCastAdapter
        })
    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestLocationUpdates() {
        locationHelper.requestLocationUpdates { location ->
            // Log latitude and longitude here
            val latitude = location.latitude
            val longitude = location.longitude

            viM.getForecastUpcoming(null, latitude.toString(), longitude.toString())
            logLocation(latitude, longitude)
        }
    }

    private fun logLocation(latitude: Double, longitude: Double) {
        // Log the latitude and longitude
        val message = "Latitude: $latitude, Longitude: $longitude"

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
                // Permission granted, request location updates
                requestLocationUpdates()
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(
                    this,
                    "Location permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }



}