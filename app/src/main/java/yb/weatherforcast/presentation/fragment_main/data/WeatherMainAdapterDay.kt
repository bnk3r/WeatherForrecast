package yb.weatherforcast.presentation.fragment_main.data

data class WeatherMainAdapterDay(
    var day: String,
    var minTemp: Int,
    var maxTemp: Int,
    var description: String,
    var icon: String
)