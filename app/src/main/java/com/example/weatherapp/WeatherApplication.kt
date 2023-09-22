package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.util.SharedPrefs

class WeatherApplication : Application() {

    companion object{
        lateinit var instance : WeatherApplication
    }

    override fun onCreate(){
        super.onCreate()
        instance = this
    }

    override fun onTerminate() {
        super.onTerminate()
        val sharedPrefs = SharedPrefs.getInstance(this)
        sharedPrefs.clearCityValue()
    }

}