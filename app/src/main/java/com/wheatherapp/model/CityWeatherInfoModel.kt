package com.wheatherapp.model

import com.google.gson.annotations.SerializedName

data class CityWeatherInfoModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("coord")
    var coord: CoordinateModel,
    @SerializedName("main")
    var forecastMain: ForecastModel,
    @SerializedName("dt")
    var unixDateTime: Long,
    @SerializedName("wind")
    var wind: WindNetworkModel?,
    @SerializedName("weather")
    var weather: List<WeatherNetworkModel>,
    @SerializedName("clouds")
    var clouds: CloudsModel?
)
