package com.wheatherapp.model

import com.google.gson.annotations.SerializedName

data class CloudsModel(
    @SerializedName("all")
    var all: Int
)