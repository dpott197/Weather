package com.dpott197.weather.adapter

import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dpott197.weather.R

import com.dpott197.weather.model.WeatherList
import java.text.SimpleDateFormat
import java.util.Locale

class ForecastAdapter : RecyclerView.Adapter<ForecastHolder>() {

    private var listofforecast = listOf<WeatherList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fourdaylistitem, parent, false)
        return ForecastHolder(view)
    }

    override fun getItemCount(): Int {
        return listofforecast.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {
        val forecastObject = listofforecast[position]

        for (i in forecastObject.weather){
            holder.description.text = i.description!!
        }

        holder.humiditiy.text = forecastObject.main!!.humidity.toString()
        holder.windspeed.text = forecastObject.wind?.speed.toString()

        val temperatureFahrenheit = forecastObject.main?.temp
        val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted = String.format("%.2f", temperatureCelsius)

        holder.temp.text = "$temperatureFormatted °C"

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = inputFormat.parse(forecastObject.dtTxt!!)
        val outputFormat = SimpleDateFormat("d MMMM EEEE", Locale.getDefault())
         val dateanddayname = outputFormat.format(date!!)

        holder.dateDayName.text = dateanddayname

        for (weather in forecastObject.weather) {
            if (weather.icon != null && !TextUtils.isEmpty(weather.icon)) {
                val weatherIcon = WeatherIcon.fromIconCode(weather.icon!!)
                holder.imageGraphic.setImageResource(weatherIcon!!.drawableRes)
                holder.smallIcon.setImageResource(weatherIcon!!.drawableRes)
            }
        }
    }

    fun setList(newlist: List<WeatherList>) {
        this.listofforecast = newlist
    }
}

class ForecastHolder(itemView: View) : ViewHolder(itemView){
    val imageGraphic: ImageView = itemView.findViewById(R.id.imageGraphic)
    val description : TextView = itemView.findViewById(R.id.weatherDescr)
    val humiditiy : TextView = itemView.findViewById(R.id.humidity)
    val windspeed : TextView = itemView.findViewById(R.id.windSpeed)
    val temp : TextView = itemView.findViewById(R.id.tempDisplayForeCast)
    val smallIcon : ImageView = itemView.findViewById(R.id.smallIcon)
    val dateDayName : TextView = itemView.findViewById(R.id.dayDateText)
}

