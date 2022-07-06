package yb.weatherforcast.app.util

import java.util.*

val countries = Locale.getISOCountries().map { locale ->
    Locale("", locale).displayCountry.lowercase()
}.sorted() // alphabetically

// Search for countries matching text param.
// Return a list of all country that match the given text.
fun List<String>.byName(text: String) : List<String> {
    return countries.filter { country ->
        country.contains(text, true)
    }
}
