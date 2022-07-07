package yb.weatherforcast.app.util

import com.google.gson.Gson
import org.json.JSONObject
import yb.weatherforcast.data.remote.WeatherResponse
import yb.weatherforcast.data.data_source.WeatherDataSource
import java.util.*

fun JSONObject.weatherJsonObjToDataSource(): WeatherDataSource {
    val resObj = Gson().fromJson(toString(), WeatherResponse::class.java)
    val days = resObj.list.weatherJsonObjToDataSource().groupByDay()
    return WeatherDataSource(
        requestTime = Calendar.getInstance().time,
        city = WeatherDataSource.City(
            apiID = resObj.city.id,
            name = resObj.city.name,
            countryCode = resObj.city.country,
            coordinates = WeatherDataSource.Coordinates(
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