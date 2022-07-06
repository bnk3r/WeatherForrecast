package yb.weatherforcast.app.injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import yb.weatherforcast.domain.use_case.getWeather.GetWeatherUseCase
import yb.weatherforcast.presentation.fragment_main.MainFragmentViewModel
import yb.weatherforcast.presentation.fragment_main.WeatherDaysAdapter

@Module
@InstallIn(FragmentComponent::class)
object MainFragmentModule {

    @Provides
    fun providesMainFragmentViewModel(
        getWeatherUseCase: GetWeatherUseCase
    ): MainFragmentViewModel {
        return MainFragmentViewModel(getWeatherUseCase)
    }

    @Provides
    fun providesWeatherDaysAdapter(): WeatherDaysAdapter {
        return WeatherDaysAdapter()
    }

}