package yb.weatherforcast.models.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import yb.weatherforcast.models.business.Weather
import yb.weatherforcast.models.business.WeatherError

@Parcelize
data class WeatherData(
    var progress: Boolean = false,
    var weather: Weather? = null,
    var error: WeatherError? = null
) : Parcelable