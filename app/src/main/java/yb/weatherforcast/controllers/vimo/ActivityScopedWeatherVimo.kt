package yb.weatherforcast.controllers.vimo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import yb.weatherforcast.controllers.repos.WeatherRepository
import yb.weatherforcast.models.business.Weather
import yb.weatherforcast.models.business.WeatherError
import yb.weatherforcast.models.business.formatLog
import yb.weatherforcast.models.ui.WeatherData
import yb.weatherforcast.views.main.IWeatherDisplayerContract
import javax.inject.Inject

@HiltViewModel
class ActivityScopedWeatherVimo
@Inject constructor(
    private val weatherRepo: WeatherRepository
) : WeatherCollectorVimo(), IWeatherDisplayerContract.Vimo {

    var weatherData = MutableLiveData<WeatherData>().apply {
        value = WeatherData()
    }

    override fun findWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherData.postValue(WeatherData(progress = true))
            weatherRepo.getWeather(weatherCollectorVimo = this@ActivityScopedWeatherVimo)
        }
    }

    override suspend fun treatSuccess(weather: Weather) {
        weatherData.postValue(WeatherData(progress = false, weather = weather))
    }

    override suspend fun treatError(error: WeatherError) {
        Log.e(javaClass.simpleName, error.formatLog())
        weatherData.postValue(WeatherData(progress = false, error = error))
    }

}