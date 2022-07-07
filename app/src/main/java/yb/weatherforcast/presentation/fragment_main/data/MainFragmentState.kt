package yb.weatherforcast.presentation.fragment_main.data

import android.view.View
import yb.weatherforcast.data.data_source.WeatherDataSource
import yb.weatherforcast.domain.model.BusinessDataState

data class MainFragmentState(
    val getWeatherState: BusinessDataState,
    val headerVisibility: Int = View.GONE,
    val currentCity: String? = null,
    val currentTempText: String? = null,
    val currentTempIconUrl: String? = null,
    val nextFiveDays: List<WeatherDataSource.Day> = listOf(),
    val getWeatherErrorIconId: Int? = null,
    val getWeatherErrorTitleId: Int? = null,
    val getWeatherErrorMessageId: Int? = null,
)
