package com.sherepenko.android.launchiteasy.providers

import com.sherepenko.android.launchiteasy.api.OpenWeatherApi
import com.sherepenko.android.launchiteasy.data.ForecastItem
import com.sherepenko.android.launchiteasy.data.WeatherItem
import com.sherepenko.android.launchiteasy.data.db.AppDatabase

interface WeatherDataSource {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherItem

    suspend fun getWeatherForecasts(latitude: Double, longitude: Double): List<ForecastItem>
}

class WeatherLocalDataSource(
    private val database: AppDatabase,
    private val forecastsCount: Int
) : WeatherDataSource {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherItem =
        database.getWeatherDao().getCurrentWeather()

    override suspend fun getWeatherForecasts(
        latitude: Double,
        longitude: Double
    ): List<ForecastItem> =
        database.getForecastDao().getWeatherForecasts(forecastsCount)

    suspend fun saveCurrentWeather(weather: WeatherItem) {
        database.getWeatherDao().updateCurrentWeather(weather)
    }

    suspend fun saveWeatherForecasts(forecasts: List<ForecastItem>) {
        database.getForecastDao().updateWeatherForecasts(*forecasts.toTypedArray())
    }
}

class WeatherRemoteDataSource(private val weatherApi: OpenWeatherApi) :
    WeatherDataSource {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherItem =
        weatherApi.getCurrentWeatherByLocation(latitude, longitude).currentWeather

    override suspend fun getWeatherForecasts(
        latitude: Double,
        longitude: Double
    ): List<ForecastItem> =
        weatherApi.getWeatherForecastsByLocation(latitude, longitude).weatherForecasts
}
