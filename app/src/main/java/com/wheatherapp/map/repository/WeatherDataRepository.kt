package com.wheatherapp.map.repository

import com.wheatherapp.di.database.dao.BookmarkDao
import com.wheatherapp.di.database.entities.Bookmark
import com.wheatherapp.di.network.ApiService
import com.wheatherapp.model.CityWeatherInfoModel
import com.wheatherapp.model.LocationWeatherModel
import javax.inject.Inject

class WeatherDataRepository @Inject constructor(val apiService: ApiService, val dao: BookmarkDao) {

    suspend fun getTodayWeatherData(lat: Double, lon: Double): CityWeatherInfoModel {
        return apiService.getTodayWeatherData(lat, lon)

    }

    suspend fun insertBookmark(locationWeather: LocationWeatherModel) = dao.insert(
        Bookmark(
            id = locationWeather.id,
            latitude = locationWeather.lat,
            longitude = locationWeather.lon
        )
    )

    suspend fun deleteBookmark(locationWeather: LocationWeatherModel) = dao.delete(
        Bookmark(
            id = locationWeather.id,
            latitude = locationWeather.lat,
            longitude = locationWeather.lon
        )
    )


    suspend fun getBookMark() = dao.getAll()

    suspend fun containBookMark(id:Int):List<Bookmark> = dao.contain(id)

}
