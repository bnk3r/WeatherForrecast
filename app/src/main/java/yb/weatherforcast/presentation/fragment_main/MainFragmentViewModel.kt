package yb.weatherforcast.presentation.fragment_main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import yb.weatherforcast.R
import yb.weatherforcast.app.util.formatCelsius
import yb.weatherforcast.domain.model.BusinessDataState.*
import yb.weatherforcast.domain.model.WeatherError.Type.*
import yb.weatherforcast.domain.model.WeatherState
import yb.weatherforcast.domain.use_case.getWeather.GetWeatherUseCase
import yb.weatherforcast.presentation.fragment_main.data.MainFragmentState
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    var fragmentState = MutableLiveData<MainFragmentState>().apply {
        value = MainFragmentState(
            getWeatherState = IDLE
        )
    }

    private var weatherLiveData: LiveData<WeatherState>? = null

    private val weatherObserver = Observer<WeatherState?> { weatherState ->
        weatherState?.let { observedWeatherState ->
            when (observedWeatherState.stateOfGet) {
                SUCCESS -> onGetWeatherSuccess(observedWeatherState)
                ERROR -> onGetWeatherError(observedWeatherState)
                else -> fragmentState.postValue(MainFragmentState(getWeatherState = UNKNOWN_STATE))
            }
        }
    }

    override fun onCleared() {
        weatherLiveData?.removeObserver(weatherObserver)
        weatherLiveData = null
        super.onCleared()
    }

    private fun onGetWeatherSuccess(weatherState: WeatherState) {
        weatherState.weather?.let { weather ->
            fragmentState.postValue(
                MainFragmentState(
                    getWeatherState = weatherState.stateOfGet,
                    currentCity = weather.city.name,
                    currentTempText = weather.currentTemperature.formatCelsius(),
                    currentTempIconUrl = "http://openweathermap.org/img/wn/${weather.currentTemperatureIcon}@2x.png",
                    nextFiveDays = weather.days.subList(0, 5)
                )
            )
        } ?: fragmentState.postValue(MainFragmentState(getWeatherState = UNKNOWN_STATE))
    }

    private fun onGetWeatherError(weatherState: WeatherState) {
        weatherState.errorOfGet?.let { error ->
            fragmentState.postValue(
                MainFragmentState(
                    getWeatherState = weatherState.stateOfGet,
                    getWeatherErrorIconId = when (error.type) {
                        VOLLEY -> R.drawable.ic_baseline_wifi_off_24
                        JSON, NPE, UNKNOWN -> R.drawable.ic_baseline_error_outline_24
                    },
                    getWeatherErrorTitleId = when (error.type) {
                        VOLLEY -> R.string.error_network_title
                        JSON, NPE, UNKNOWN -> R.string.error_computation_title
                    },
                    getWeatherErrorMessageId = when (error.type) {
                        VOLLEY -> R.string.error_network_message
                        JSON, NPE, UNKNOWN -> R.string.error_computation_message
                    }
                )
            )
        } ?: fragmentState.postValue(MainFragmentState(getWeatherState = UNKNOWN_STATE))
    }

    /**
     * Live data to observe : [fragmentState]
     */
    fun getWeather(city: String) {
        Log.i("GET_WEATHER", "MainFragmentViewModel getWeather $city")
        fragmentState.value = MainFragmentState(getWeatherState = LOADING)
        weatherLiveData = getWeatherUseCase(city)
        weatherLiveData?.observeForever(weatherObserver)
    }

}