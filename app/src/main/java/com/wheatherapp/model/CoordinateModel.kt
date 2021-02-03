package com.wheatherapp.model

import com.google.gson.annotations.SerializedName

data class CoordinateModel(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double
)
