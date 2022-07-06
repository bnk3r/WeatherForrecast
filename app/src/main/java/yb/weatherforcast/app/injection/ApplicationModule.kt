package yb.weatherforcast.app.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import yb.weatherforcast.app.util.ResourcesProvider

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun providesResourcesProvider(
        @ApplicationContext context: Context
    ) : ResourcesProvider {
        return ResourcesProvider(context)
    }

}