package yb.weatherforcast.models.business

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherError(
    val type: Type,
    val msg: String = ""
) : Parcelable {

    enum class Type(val value: String) {
        NPE("NullPointerException"),
        JSON("JSONException"),
        VOLLEY("VolleyException"),
        UNKNOWN("UnknownExcpetion")
    }

}

fun WeatherError.formatLog() = "Error while fetching weather data ($type): ($msg)"