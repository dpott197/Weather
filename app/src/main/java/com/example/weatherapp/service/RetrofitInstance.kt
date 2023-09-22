package com.example.weatherapp.service

import android.util.Log
import com.example.weatherapp.util.Utils
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private val prettyPrintInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            val gson = GsonBuilder().setPrettyPrinting().create()

            override fun log(message: String) {
                try {
                    val jsonElement = JsonParser.parseString(message)
                    val prettyJson = gson.toJson(jsonElement)
                    Log.d("RetrofitInstance", prettyJson)
                } catch (exception: Exception) {
                    Log.e("RetrofitInstance", message, exception)
                }
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val retrofit by lazy {
            val client = OkHttpClient.Builder().addInterceptor(prettyPrintInterceptor).build()

            Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(Service::class.java)
        }
    }

}