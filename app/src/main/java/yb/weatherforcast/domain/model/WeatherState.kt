package yb.weatherforcast.domain.model

import yb.weatherforcast.data.data_source.WeatherDataSource

data class WeatherState(
    var stateOfGet: BusinessDataState = BusinessDataState.IDLE,
    var weather: WeatherDataSource? = null,
    var errorOfGet: WeatherError? = null,
)