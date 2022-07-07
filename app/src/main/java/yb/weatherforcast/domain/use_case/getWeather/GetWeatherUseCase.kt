package yb.weatherforcast.domain.use_case.getWeather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Response
import com.android.volley.VolleyError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import yb.weatherforcast.app.util.weatherJsonObjToDataSource
import yb.weatherforcast.domain.model.BusinessDataState
import yb.weatherforcast.domain.model.WeatherError
import yb.weatherforcast.domain.model.WeatherState
import yb.weatherforcast.domain.repository.WeatherRepositoryImpl

class GetWeatherUseCase(
    private val repository: WeatherRepositoryImpl
) : Response.Listener<JSONObject>, Response.ErrorListener {

    private val weatherStateLiveData = MutableLiveData<WeatherState?>().apply { value = null }

    operator fun invoke(city: String): LiveData<WeatherState> {
        val livedata = MutableLiveData<WeatherState>().apply { value = null }
        Log.i("GET_WEATHER", "GetWeatherUseCase invoked $city")
        repository.getWeather(city, this, this)
        weatherStateLiveData.observeForever { weatherState ->
            weatherState?.let { observedWeatherState ->
                livedata.postValue(observedWeatherState)
            }
        }
        return livedata
    }

    override fun onResponse(response: JSONObject?) {
        CoroutineScope(Dispatchers.Default).launch {
            Log.i("GET_WEATHER", "receive json object  : $response")
            try {
                val weather = response?.weatherJsonObjToDataSource()
                weatherStateLiveData.postValue(
                    WeatherState(BusinessDataState.SUCCESS, weather = weather)
                )
            } catch (e: JSONException) {
                val err = WeatherError(WeatherError.Type.JSON, e.message ?: "")
                weatherStateLiveData.postValue(
                    WeatherState(BusinessDataState.ERROR, errorOfGet = err)
                )
            } catch (e: NullPointerException) {
                val err = WeatherError(WeatherError.Type.NPE, e.message ?: "")
                weatherStateLiveData.postValue(
                    WeatherState(BusinessDataState.ERROR, errorOfGet = err)
                )
            } catch (e: Exception) {
                val err = WeatherError(WeatherError.Type.UNKNOWN, e.message ?: "")
                weatherStateLiveData.postValue(
                    WeatherState(BusinessDataState.ERROR, errorOfGet = err)
                )
            }
        }

    }

    override fun onErrorResponse(error: VolleyError?) {
        CoroutineScope(Dispatchers.Default).launch {
            Log.i("GET_WEATHER", "receive error : ${error?.message}")
            val err = WeatherError(WeatherError.Type.VOLLEY, error.toString())
            weatherStateLiveData.postValue(
                WeatherState(BusinessDataState.ERROR, errorOfGet = err)
            )
        }
    }

}