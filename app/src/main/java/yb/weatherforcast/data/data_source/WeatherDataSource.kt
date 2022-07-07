package yb.weatherforcast.data.data_source

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import yb.weatherforcast.app.util.format
import yb.weatherforcast.app.util.formatDay
import java.util.*

@Parcelize
data class WeatherDataSource(
    val requestTime: Date,
    val requestTimeDisplay: String = requestTime.format(),
    val city: City,
    val currentTemperature: Double,
    val currentTemperatureIcon: String,
    val days: List<Day>
) : Parcelable {

    @Parcelize
    data class City(
        val apiID: Int,
        val name: String,
        val countryCode: String,
        val coordinates: Coordinates,
        val timezone: Int
    ) : Parcelable

    @Parcelize
    data class Coordinates(
        val longitude: Double,
        val latitude: Double
    ) : Parcelable

    @Parcelize
    data class Day(
        val date: Date,
        var dateDisplay: String = date.format(),
        var dayOfTheWeek: String = date.formatDay(),
        var temperatureMin: Int = 0,
        var temperatureMax: Int = 0,
        var weatherIconApiID: String = "",
        var periods: MutableList<Period> = mutableListOf()
    ) : Parcelable

    @Parcelize
    data class Period(
        val date: Date,
        val dateDisplay: String,
        val dayOfTheWeek: String,
        val hour: String,
        val temperatureKelvin: Double,
        val temperatureCelsius: Double,
        val temperatureFeelsLike: Double,
        val temperatureMin: Double,
        val temperatureMax: Double,
        val atmosphericPressure: Int,
        val seaLevel: Int,
        val groundLevel: Int,
        val humidityLevel: Int,
        val weatherInfoApiID: String,
        val weatherOverview: String,
        val weatherDescription: String,
        val weatherIconApiID: String,
        val windSpeed: Double,
        val windDegree: Int,
        val windGusts: Double,
        val percentCloudCover: Int
    ) : Parcelable

}