package yb.weatherforcast.controllers.weather

// Format for display celcius
fun Int.formatCelcius() = "$this°C"
fun Double.formatCelsius() = "${String.format("%.2f", this)}°C"

// Format for display kelvin
fun Int.formatKelvin() = "$this°K"
fun Double.formatKelvin() = "${String.format("%.2f", this)}°K"