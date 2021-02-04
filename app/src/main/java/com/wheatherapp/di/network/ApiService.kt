package com.wheatherapp.di.network

import com.wheatherapp.model.CityWeatherInfoModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather/")
    suspend fun getTodayWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") appid: String? = "20b9b47a8ad771fa8f6dc38f5a8e84c0"
    ): CityWeatherInfoModel
}
