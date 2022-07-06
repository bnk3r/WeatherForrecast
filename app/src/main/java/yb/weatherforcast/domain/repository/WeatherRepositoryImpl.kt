package yb.weatherforcast.domain.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import yb.weatherforcast.R
import yb.weatherforcast.data.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val requestQueue: RequestQueue
) : WeatherRepository {

    override fun mountApiUrl(city: String): String {
        return (context.getString(R.string.root_url) +
                context.getString(R.string.url_forecast) +
                "?q=${city}&limit=1" +
                "&APPID=" + context.getString(R.string.param_token))
    }

    override fun getWeather(
        city: String,
        responseListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        Log.i("GET_WEATHER", "WeatherRepositoryImpl getWeather $city")
        requestQueue.add(
            JsonObjectRequest(
                Request.Method.GET,
                Uri.parse(mountApiUrl(city)).toString(),
                null,
                responseListener,
                errorListener
            )
        )
    }

}