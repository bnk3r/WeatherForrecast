package yb.weatherforcast.data.repository

import com.android.volley.Response
import org.json.JSONObject

interface WeatherRepository {

    fun mountApiUrl(city: String): String

    fun getWeather(
        city: String,
        responseListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    )

}