package com.dpott197.weather.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dpott197.weather.R
import com.dpott197.weather.adapter.CurrentWeatherAdapter
import com.dpott197.weather.databinding.TestlayoutBinding
import com.dpott197.weather.model.WeatherList
import com.dpott197.weather.mvvm.WeatherViewModel
import com.dpott197.weather.service.LocationHelper
import com.dpott197.weather.util.NotificationHelper
import com.dpott197.weather.util.SharedPrefs
import com.dpott197.weather.util.Utils
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    lateinit var weatherViewModel: WeatherViewModel
    lateinit var currentWeatherAdapter: CurrentWeatherAdapter

    private lateinit var binding: TestlayoutBinding
    private lateinit var locationHelper: LocationHelper

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val notificationhelper = NotificationHelper(this)

        binding = DataBindingUtil.setContentView(this, R.layout.testlayout)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        binding.lifecycleOwner = this
        binding.vm = weatherViewModel

        locationHelper = LocationHelper(this)

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
        currentWeatherAdapter = CurrentWeatherAdapter()

        val sharedPrefs = SharedPrefs.getInstance(this@MainActivity)
        sharedPrefs.clearCityValue()

        weatherViewModel.todayWeatherLiveData.observe(this, Observer {
            val setNewlist = it as List<WeatherList>
            Log.e("TODayweather list", it.toString())
            currentWeatherAdapter.setList(setNewlist)
            binding.forecastRecyclerView.adapter = currentWeatherAdapter
        })

        weatherViewModel.closetorexactlysameweatherdata.observe(this, Observer {
            val temperatureFahrenheit = it!!.main?.temp
            val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
            val temperatureFormatted = String.format("%.2f", temperatureCelsius)

            for (i in it.weather) {
                binding.descMain.text = i.description

                if (i.main.toString() == "Rain" ||
                    i.main.toString() == "Drizzle" ||
                    i.main.toString() == "Thunderstorm" ||
                    i.main.toString() == "Clear"){

                    notificationhelper.startNotification()

                    Log.e("MAIN", i.main.toString())
                }
            }

            binding.tempMain.text = "$temperatureFormatted°"

            binding.humidityMain.text = it.main!!.humidity.toString()
            binding.windSpeed.text = it.wind?.speed.toString()

            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = inputFormat.parse(it.dtTxt!!)
            val outputFormat = SimpleDateFormat("d MMMM EEEE", Locale.getDefault())
            val dateanddayname = outputFormat.format(date!!)

            binding.dateDayMain.text = dateanddayname
            binding.chanceofrain.text = "${it.pop.toString()}%"

            // setting the icon
            for (i in it.weather) {
                if (i.icon == "01d") {
                    binding.imageMain.setImageResource(R.drawable.oned)
                }

                if (i.icon == "01n") {
                    binding.imageMain.setImageResource(R.drawable.onen)
                }

                if (i.icon == "02d") {
                    binding.imageMain.setImageResource(R.drawable.twod)
                }

                if (i.icon == "02n") {
                    binding.imageMain.setImageResource(R.drawable.twon)
                }

                if (i.icon == "03d" || i.icon == "03n") {
                    binding.imageMain.setImageResource(R.drawable.threedn)
                }

                if (i.icon == "10d") {
                    binding.imageMain.setImageResource(R.drawable.tend)
                }

                if (i.icon == "10n") {
                    binding.imageMain.setImageResource(R.drawable.tenn)
                }

                if (i.icon == "04d" || i.icon == "04n") {
                    binding.imageMain.setImageResource(R.drawable.fourdn)
                }

                if (i.icon == "09d" || i.icon == "09n") {
                    binding.imageMain.setImageResource(R.drawable.ninedn)
                }

                if (i.icon == "11d" || i.icon == "11n") {
                    binding.imageMain.setImageResource(R.drawable.elevend)
                }

                if (i.icon == "13d" || i.icon == "13n") {
                    binding.imageMain.setImageResource(R.drawable.thirteend)
                }

                if (i.icon == "50d" || i.icon == "50n") {
                    binding.imageMain.setImageResource(R.drawable.fiftydn)
                }
            }
        })

        val searchEditText =
            binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.WHITE)

        binding.next5Days.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val sharedPrefs = SharedPrefs.getInstance(this@MainActivity)
                sharedPrefs.setValueOrNull("city", query!!)

                if (!query.isNullOrEmpty()) {
                    weatherViewModel.getWeather(query)
                    binding.searchView.setQuery("", false)
                    binding.searchView.clearFocus()
                    binding.searchView.isIconified = true
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestLocationUpdates()  {
        locationHelper.requestLocationUpdates { location ->
            val latitude = location.latitude
            val longitude = location.longitude

            weatherViewModel.getWeather(null, latitude.toString(), longitude.toString())
            logLocation(latitude, longitude)
        }
    }

    private fun logLocation(latitude: Double, longitude: Double) {
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

