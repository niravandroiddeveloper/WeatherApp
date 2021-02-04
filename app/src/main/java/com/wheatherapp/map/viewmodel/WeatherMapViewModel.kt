package com.wheatherapp.map.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wheatherapp.di.database.entities.Bookmark
import com.wheatherapp.model.CityWeatherInfoModel
import com.wheatherapp.model.LocationWeatherModel
import com.wheatherapp.map.repository.WeatherDataRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherMapViewModel @Inject constructor(
    private var weatherDataRepository: WeatherDataRepository
) : ViewModel() {

    var pinWeatherLiveData = MutableLiveData<LocationWeatherModel>()
    var bookmarksWeatherLiveData = MutableLiveData<List<LocationWeatherModel>>()
    var containBookMark = MutableLiveData<List<Bookmark>>()

    fun refreshPin(lat: Double, lon: Double) {
        viewModelScope.launch {
            weatherDataRepository.getTodayWeatherData(lat, lon).let {
                pinWeatherLiveData.value = it.toUiModel(lat, lon)
            }
        }
    }

    fun bookmarkLocation(locationWeather: LocationWeatherModel) {
        viewModelScope.launch {
            weatherDataRepository.insertBookmark(locationWeather)
        }
    }

    fun removeBookMark(locationWeather: LocationWeatherModel,isLoadBookMark: Boolean) {
        viewModelScope.launch {
            weatherDataRepository.deleteBookmark(locationWeather)
            if(isLoadBookMark)
            loadBookmarks()
        }
    }

    fun loadBookmarks() {
        viewModelScope.launch {
            bookmarksWeatherLiveData.value = weatherDataRepository.getBookMark()
                .map { bookmark ->
                    weatherDataRepository
                        .getTodayWeatherData(bookmark.latitude, bookmark.longitude)
                        .toUiModel(bookmark.latitude, bookmark.longitude)
                }
        }
    }

    fun containBookMarkInDatabase(id:Int) {
        viewModelScope.launch {
            weatherDataRepository.containBookMarkInDatabase(id).let {
               containBookMark.value=it
            }
        }
    }

    private fun CityWeatherInfoModel.toUiModel(lat: Double, long: Double) =
        com.wheatherapp.model.LocationWeatherModel(
            id = this.id,
            name = this.name,
            lat = lat,
            lon = long,
            windSpeed = this.wind?.speed,
            windDegree = this.wind?.degree,
            temp = this.forecastMain.temp.toInt(),
            tempMax = this.forecastMain.tempMax.toInt(),
            tempMin = this.forecastMain.tempMin.toInt(),
            humidity = this.forecastMain.humidity,
            pressure = this.forecastMain.pressure,
            clouds = this.clouds?.all ?: 0
        )
}
