package yb.weatherforcast.presentation.weather

import android.view.View
import yb.weatherforcast.data.data_source.WeatherDataSource
import yb.weatherforcast.domain.model.BusinessDataState
import yb.weatherforcast.domain.model.WeatherError

data class MainFragmentState(
    val getWeatherState: BusinessDataState,
    val headerVisibility: Int = View.GONE,
    val currentCity: String? = null,
    val currentTempText: String? = null,
    val currentTempIconUrl: String? = null,
    val nextFiveDays: List<WeatherDataSource.Day> = listOf(),
    val getWeatherError: WeatherError? = null,
)
