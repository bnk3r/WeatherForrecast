package yb.weatherforcast.app.util

import java.text.SimpleDateFormat
import java.util.*

fun String.parseApiDate(): Date = SimpleDateFormat(WEATHER_API_DATE_FORMAT, Locale.FRANCE)
    .parse(this) ?: throw IllegalStateException("Unable to parse date (value=\"$this\").")