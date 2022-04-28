package yb.weatherforcast.app.injection

import android.content.Context
import com.android.volley.Cache
import com.android.volley.Network
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providesNetwork() : Network {
        return BasicNetwork(HurlStack())
    }

    @Provides
    fun providesCache(@ApplicationContext context: Context): Cache {
        return DiskBasedCache(context.cacheDir, 1024 * 1024) //1Mo per response
    }

    @Provides
    fun providesRequestQueue(cache: Cache, network: Network) : RequestQueue {
        return RequestQueue(cache, network).also {
            it.start()
        }
    }
}