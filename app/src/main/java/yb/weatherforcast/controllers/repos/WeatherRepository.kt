package yb.weatherforcast.controllers.repos

import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import yb.weatherforcast.R
import yb.weatherforcast.controllers.vimo.WeatherCollectorVimo
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val requestQueue: RequestQueue
) {

    private fun getWeatherURL(): String {
        return (context.getString(R.string.root_url) +
                context.getString(R.string.url_forecast) +
                "?q=Rennes&limit=1" + //context.getString(R.string.param_id_paris) + //TODO remove
                context.getString(R.string.param_token))
    }

    fun getWeather(weatherCollectorVimo: WeatherCollectorVimo) {
        requestQueue.add(
            JsonObjectRequest(
                Request.Method.GET,
                Uri.parse(getWeatherURL()).toString(),
                null,
                weatherCollectorVimo,
                weatherCollectorVimo
            )
        )
    }

}