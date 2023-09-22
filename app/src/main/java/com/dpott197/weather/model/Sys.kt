package com.dpott197.weather.model

import com.google.gson.annotations.SerializedName

data class Sys (
  @SerializedName("pod" ) var pod : String? = null
)
