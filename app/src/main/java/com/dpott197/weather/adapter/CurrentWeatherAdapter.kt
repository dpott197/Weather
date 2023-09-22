package com.dpott197.weather.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.dpott197.weather.R
import com.dpott197.weather.model.WeatherList
import java.text.SimpleDateFormat
import java.util.Calendar

class CurrentWeatherAdapter : RecyclerView.Adapter<TodayHolder>() {

    private var listOfTodayWeather = listOf<WeatherList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.todayforecastlist, parent, false)
        return TodayHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfTodayWeather.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TodayHolder, position: Int) {
        val todayForeCast = listOfTodayWeather[position]
        holder.timeDisplay.text = todayForeCast.dtTxt!!.substring(11, 16).toString()

        val temperatureFahrenheit = todayForeCast.main?.temp
        val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted = String.format("%.2f", temperatureCelsius)

        holder.tempDisplay.text = "$temperatureFormatted °C"
        val calendar = Calendar.getInstance()

        // Define the desired format
        val dateFormat = SimpleDateFormat("HH::mm")
        val formattedTime = dateFormat.format(calendar.time)

        val timeofapi = todayForeCast.dtTxt!!.split(" ")
        val partafterspace = timeofapi[1]

        Log.e("time" , " formatted time:${formattedTime}, timeofapi: ${partafterspace}")

        for (weather in todayForeCast.weather){
            if (weather.icon != null && !TextUtils.isEmpty(weather.icon)) {
                val weatherIcon = WeatherIcon.fromIconCode(weather.icon!!)
                holder.imageDisplay.setImageResource(weatherIcon!!.drawableRes)
            }
        }
    }

    fun setList(listOfToday: List<WeatherList>) {
        this.listOfTodayWeather = listOfToday
    }
}

class TodayHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val imageDisplay : ImageView = itemView.findViewById(R.id.imageDisplay)
    val tempDisplay : TextView = itemView.findViewById(R.id.tempDisplay)
    val timeDisplay : TextView = itemView.findViewById(R.id.timeDisplay)
}



