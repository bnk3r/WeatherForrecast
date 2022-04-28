package yb.weatherforcast.app.injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import yb.weatherforcast.controllers.repos.WeatherRepository
import yb.weatherforcast.controllers.vimo.ActivityScopedWeatherVimo
import yb.weatherforcast.views.main.WeatherDaysAdapter

@Module
@InstallIn(ActivityComponent::class)
object MainScreenModule {

    @Provides
    fun providesWeatherVimo(
        repo: WeatherRepository
    ) : ActivityScopedWeatherVimo {
        return ActivityScopedWeatherVimo(repo)
    }

    @Provides
    fun providesWeatherDaysAdapter() : WeatherDaysAdapter {
        return WeatherDaysAdapter()
    }

}