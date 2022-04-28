package yb.weatherforcast.controllers.vimo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import com.android.volley.VolleyError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import yb.weatherforcast.controllers.network.jsonObjToBusinessClass
import yb.weatherforcast.models.business.Weather
import yb.weatherforcast.models.business.WeatherError

abstract class WeatherCollectorVimo :
    ViewModel(),
    Response.Listener<JSONObject>,
    Response.ErrorListener {

    override fun onResponse(response: JSONObject?) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                treatSuccess(response!!.jsonObjToBusinessClass())
            } catch (e: JSONException) {
                treatError(WeatherError(WeatherError.Type.JSON, e.message ?: ""))
            } catch (e: NullPointerException) {
                treatError(WeatherError(WeatherError.Type.NPE, e.message ?: ""))
            } catch (e: Exception) {
                treatError(WeatherError(WeatherError.Type.UNKNOWN, e.message ?: ""))
            }
        }
    }

    override fun onErrorResponse(error: VolleyError?) {
        viewModelScope.launch(Dispatchers.Default) {
            treatError(WeatherError(WeatherError.Type.VOLLEY, error.toString()))
        }
    }

    abstract suspend fun treatSuccess(weather: Weather)

    abstract suspend fun treatError(error: WeatherError)

}