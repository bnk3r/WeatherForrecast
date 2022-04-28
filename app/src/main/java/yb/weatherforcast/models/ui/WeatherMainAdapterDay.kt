package yb.weatherforcast.models.ui

import yb.weatherforcast.models.business.Weather

data class WeatherMainAdapterDay(
    var day: String,
    var minTemp: Int,
    var maxTemp: Int,
    var description: String,
    var icon: String
)

fun Weather.extractMainAdatperDays() = days.map {
    WeatherMainAdapterDay(
        day = it.dayOfTheWeek,
        minTemp = it.temperatureMin,
        maxTemp = it.temperatureMax,
        description = it.periods[0].weatherDescription,
        icon = it.weatherIconApiID
    )
}