package yb.weatherforcast.app.injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yb.weatherforcast.domain.repository.WeatherRepositoryImpl
import yb.weatherforcast.domain.use_case.getWeather.GetWeatherUseCase

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    @Provides
    fun providesGetWeatherUseCase(
        repo: WeatherRepositoryImpl
    ) : GetWeatherUseCase {
        return GetWeatherUseCase(repo)
    }

}