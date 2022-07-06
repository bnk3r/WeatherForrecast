package yb.weatherforcast.app.util

import yb.weatherforcast.data.remote.Period
import yb.weatherforcast.data.data_source.WeatherDataSource
import kotlin.math.ceil
import kotlin.math.floor

const val KELVIN_RATIO = 273.15

// Converts periods (JSON objects) into days (Business data classes)
fun List<Period>.weatherJsonObjToDataSource(): List<WeatherDataSource.Period> = map { period ->
    val d = period.dtTxt.parseApiDate()
    WeatherDataSource.Period(
        date = d,
        dateDisplay = d.format(),
        dayOfTheWeek = d.formatDay(),
        hour = d.formatHour(),
        temperatureKelvin = period.main.temp,
        temperatureCelsius = period.main.temp.kelvinToCelsius(),
        temperatureFeelsLike = period.main.feelsLike,
        temperatureMin = period.main.tempMin,
        temperatureMax = period.main.tempMax,
        atmosphericPressure = period.main.pressure,
        seaLevel = period.main.seaLevel,
        groundLevel = period.main.grndLevel,
        humidityLevel = period.main.humidity,
        weatherInfoApiID = period.weather.getOrNull(0)?.icon ?: "",
        weatherOverview = period.weather.getOrNull(0)?.main ?: "",
        weatherDescription = period.weather.getOrNull(0)?.description ?: "",
        weatherIconApiID = period.weather.getOrNull(0)?.icon ?: "",
        windSpeed = period.wind.speed,
        windDegree = period.wind.deg,
        windGusts = period.wind.gust,
        percentCloudCover = period.clouds.all
    )
}

// Group periods (Business data classes) into days (Business data classes)
fun List<WeatherDataSource.Period>.groupByDay(): List<WeatherDataSource.Day> {
    val weatherDays = mutableListOf<WeatherDataSource.Day>()
    if (isEmpty()) return weatherDays
    var currentDay = get(0).dayOfTheWeek
    var weatherDay = WeatherDataSource.Day(date = get(0).date)
    for (period in this) {
        // date change = new day
        if (currentDay != period.date.formatDay()) {
            concludeDay(weatherDay)
            weatherDays.add(weatherDay)
            weatherDay = WeatherDataSource.Day(date = period.date)
            currentDay = period.date.formatDay()
        } else {
            weatherDay.periods.add(period)
        }
    }
    //For the last day
    concludeDay(weatherDay)
    weatherDays.add(weatherDay)
    return weatherDays
}

// Gathering everything to finish WeatherDay object
private fun concludeDay(day: WeatherDataSource.Day) {
    //Floor for lowest and ceil for the highest
    var lowest = Double.MAX_VALUE
    var highest = Double.MIN_VALUE
    var i = 0
    while (i < day.periods.size) {
        val temp = day.periods[i].temperatureCelsius
        if (temp < lowest)
            lowest = temp
        if (temp > highest)
            highest = temp
        ++i
    }
    day.temperatureMin = floor(lowest).toInt()
    day.temperatureMax = ceil(highest).toInt()
    //Icon for the day is the one at 12h or the 1st icon in the list for the 1st day
    day.weatherIconApiID = day.periods.firstOrNull { it.hour == "12" }?.weatherInfoApiID
        ?: day.periods.first().weatherInfoApiID
}