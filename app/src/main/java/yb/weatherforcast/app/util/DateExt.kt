package yb.weatherforcast.app.util

import java.text.SimpleDateFormat
import java.util.*

const val WEATHER_API_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

fun Date.format(): String = SimpleDateFormat("EEE dd MMM yyyy", Locale.FRANCE)
    .format(this)

fun Date.formatDay(): String = SimpleDateFormat("EEEE", Locale.FRANCE)
    .format(this)

fun Date.formatHour(): String = SimpleDateFormat("HH", Locale.FRANCE)
    .format(this)