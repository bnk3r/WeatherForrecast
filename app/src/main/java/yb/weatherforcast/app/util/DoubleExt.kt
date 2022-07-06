package yb.weatherforcast.app.util

fun Double.kelvinToCelsius() = this - KELVIN_RATIO

fun Double.formatCelsius() = "${String.format("%.2f", this)}°C"

fun Double.formatKelvin() = "${String.format("%.2f", this)}°K"