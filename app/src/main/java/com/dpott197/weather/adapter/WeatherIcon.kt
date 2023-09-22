package com.dpott197.weather.adapter

import com.dpott197.weather.R

enum class WeatherIcon(val iconCode: String, val drawableRes: Int) {
    ONED("01d", R.drawable.oned),
    ONEN("01n", R.drawable.onen),
    TWOD("02d", R.drawable.twod),
    TWON("02n", R.drawable.twon),
    THREED("03d", R.drawable.threedn),
    THREEN("03n", R.drawable.threedn),
    TEND("10d", R.drawable.tend),
    TENN("10n", R.drawable.tenn),
    FOURD("04d", R.drawable.fourdn),
    FOURN("04n", R.drawable.fourdn),
    NINED("09d", R.drawable.ninedn),
    NINEN("09n", R.drawable.ninedn),
    ELEVEND("11d", R.drawable.elevend),
    ELEVENN("11n", R.drawable.elevend),
    THIRTEEND("13d", R.drawable.thirteend),
    THIRTEENN("13n", R.drawable.thirteend),
    FIFTYD("50d", R.drawable.fiftydn),
    FIFTYN("50n", R.drawable.fiftydn);

    companion object {
        fun fromIconCode(iconCode: String): WeatherIcon? {
            return values().find { it.iconCode == iconCode }
        }
    }
}