package gaston.gsanguinetti.glovo.network.datasource.module

import gaston.gsanguinetti.glovo.network.datasource.api.NetworkApi
import gaston.gsanguinetti.glovo.network.datasource.cache.CacheProvider
import gaston.gsanguinetti.glovo.network.datasource.cache.DeviceCacheProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val networkDataSource = module {
    single { DeviceCacheProvider(androidContext()) as CacheProvider }
    factory { NetworkApi(get(), get()) }
}