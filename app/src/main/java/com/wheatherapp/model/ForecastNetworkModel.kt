package com.wheatherapp.model

import com.google.gson.annotations.SerializedName

data class ForecastNetworkModel(
    @SerializedName("dt")
    var unixDateTime: Long,
    @SerializedName("dt_txt")
    var dateTimeString: String,
    @SerializedName("main")
    var main: ForecastModel,
    @SerializedName("weather")
    var weather: List<WeatherNetworkModel>,
    @SerializedName("clouds")
    var clouds: CloudsModel?,
    @SerializedName("wind")
    var wind: WindNetworkModel?
)
