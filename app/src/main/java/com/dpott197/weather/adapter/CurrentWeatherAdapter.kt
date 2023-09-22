package com.dpott197.weather.adapter

import android.annotation.SuppressLint
import android.os.Build
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
            if (weather.icon == "01d") {
                holder.imageDisplay.setImageResource(R.drawable.oned)
            }

            if (weather.icon == "01n") {
                holder.imageDisplay.setImageResource(R.drawable.onen)
            }

            if (weather.icon == "02d") {
                holder.imageDisplay.setImageResource(R.drawable.twod)
            }

            if (weather.icon == "02n") {
                holder.imageDisplay.setImageResource(R.drawable.twon)
            }

            if (weather.icon == "03d" || weather.icon == "03n") {
                holder.imageDisplay.setImageResource(R.drawable.threedn)
            }

            if (weather.icon == "10d") {
                holder.imageDisplay.setImageResource(R.drawable.tend)
            }

            if (weather.icon == "10n") {
                holder.imageDisplay.setImageResource(R.drawable.tenn)
            }

            if (weather.icon == "04d" || weather.icon == "04n") {
                holder.imageDisplay.setImageResource(R.drawable.fourdn)
            }

            if (weather.icon == "09d" || weather.icon == "09n") {
                holder.imageDisplay.setImageResource(R.drawable.ninedn)
            }

            if (weather.icon == "11d" || weather.icon == "11n") {
                holder.imageDisplay.setImageResource(R.drawable.elevend)
            }

            if (weather.icon == "13d" || weather.icon == "13n") {
                holder.imageDisplay.setImageResource(R.drawable.thirteend)
            }

            if (weather.icon == "50d" || weather.icon == "50n") {
                holder.imageDisplay.setImageResource(R.drawable.fiftydn)
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



