package yb.weatherforcast.views.main

import yb.weatherforcast.models.business.Weather
import yb.weatherforcast.models.business.WeatherError


interface IWeatherDisplayerContract {

    interface View {
        fun showProgress(visible: Boolean)
        fun showWeather(weather: Weather?)
        fun showError(error: WeatherError?)
        fun showCityName(city: String?, progress: Boolean)
    }

    interface Vimo {
        fun findWeather()
    }

}