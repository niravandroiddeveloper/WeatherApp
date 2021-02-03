package com.wheatherapp.model

import com.google.gson.annotations.SerializedName

data class WindNetworkModel(
    @SerializedName("speed")
    var speed: Double,
    @SerializedName("deg")
    var degree: Double
)
