package yb.weatherforcast.app.injection

import android.content.Context
import com.android.volley.RequestQueue
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import yb.weatherforcast.controllers.repos.WeatherRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providesWeatherRepository(
        @ApplicationContext context: Context,
        requestQueue: RequestQueue
    ) : WeatherRepository {
        return WeatherRepository(context, requestQueue)
    }

}