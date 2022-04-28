package yb.weatherforcast.controllers.network

import com.google.gson.Gson
import org.json.JSONObject
import yb.weatherforcast.models.business.Weather
import yb.weatherforcast.models.dist.Period
import yb.weatherforcast.models.dist.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

//
// Date parsing / formatting
//

const val WEATHER_API_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

fun Date.format(): String = SimpleDateFormat("EEE dd MMM yyyy", Locale.FRANCE)
    .format(this)

fun Date.formatDay(): String = SimpleDateFormat("EEEE", Locale.FRANCE)
    .format(this)

fun Date.formatHour(): String = SimpleDateFormat("HH", Locale.FRANCE)
    .format(this)

fun String.parseApiDate(): Date = SimpleDateFormat(WEATHER_API_DATE_FORMAT, Locale.FRANCE)
    .parse(this) ?: throw IllegalStateException("Unable to parse date (value=\"$this\").")

//
// Kevin <-> Celcius
//

const val KELVIN_RATIO = 273.15
fun Double.kelvinToCelsius() = this - KELVIN_RATIO
fun Double.celsiusToKelvin() = this + KELVIN_RATIO

//
// JSON -> Business
//

fun JSONObject.jsonObjToBusinessClass(): Weather {
    val resObj = Gson().fromJson(toString(), WeatherResponse::class.java)
    val days = resObj.list.jsonObjToBusinessClass().groupByDay()
    return Weather(
        requestTime = Calendar.getInstance().time,
        city = Weather.City(
            apiID = resObj.city.id,
            name = resObj.city.name,
            countryCode = resObj.city.country,
            coordinates = Weather.Coordinates(
                longitude = resObj.city.coord.lon,
                latitude = resObj.city.coord.lat
            ),
            timezone = resObj.city.timezone
        ),
        currentTemperature = days[0].periods[0].temperatureCelsius,
        currentTemperatureIcon = days[0].periods[0].weatherIconApiID,
        days = days
    )
}

// Converts periods (JSON ojects) into days (Business data classes)
fun List<Period>.jsonObjToBusinessClass(): List<Weather.Period> = map { period ->
    val d = period.dtTxt.parseApiDate()
    Weather.Period(
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

//
// Common use cases
//

// Group periods (Business data classes) into days (Business data classes)
fun List<Weather.Period>.groupByDay(): List<Weather.Day> {
    val weatherDays = mutableListOf<Weather.Day>()
    if (isEmpty()) return weatherDays
    var currentDay = get(0).dayOfTheWeek
    var weatherDay = Weather.Day(date = get(0).date)
    for (period in this) {
        // date change = new day
        if (currentDay != period.date.formatDay()) {
            concludeDay(weatherDay)
            weatherDays.add(weatherDay)
            weatherDay = Weather.Day(date = period.date)
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

//Gathering everything to finish WeatherDay object
private fun concludeDay(day: Weather.Day) {
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