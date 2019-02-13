package gaston.gsanguinetti.glovo.network.datasource.api

import gaston.gsanguinetti.glovo.network.datasource.cache.CacheProvider
import gaston.gsanguinetti.glovo.network.datasource.model.ServerAddress
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkApi(
    private val serverAddress: ServerAddress,
    private val cacheProvider: CacheProvider
) {

    private fun getApiBuilder(): Retrofit =
        Retrofit.Builder()
            .baseUrl(serverAddress.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

    fun <DATA : Any, T> makeApiCallForResponse(
        apiClass: Class<T>,
        apiCall: ((api: T) -> Single<DATA>)
    ): Single<DATA> =
        apiCall(getApiBuilder().create(apiClass))

    fun <DATA : Any, T, CACHE_DATA :Any> makeCacheableApiCallForResponse(
        apiClass: Class<T>,
        cacheDataClass: Class<CACHE_DATA>,
        index: String,
        transformToCache :((data :DATA) -> CACHE_DATA),
        transformFromCache :((cacheData :CACHE_DATA?) -> DATA?),
        apiCall: ((api: T) -> Single<DATA>)
    ): Single<DATA> {

        return Maybe.just(true).flatMap {
            transformFromCache(cacheProvider.provide(cacheDataClass, index))?.apply { return@flatMap Maybe.just(this) }
            Maybe.empty<DATA>()
        }.switchIfEmpty(makeApiCallForResponse(apiClass, apiCall).toMaybe())
            .toSingle()
            .map {
                cacheProvider.store(transformToCache(it), index)
                it
            }
    }

    fun <DATA : Any, T> makeCacheableApiCallForResponse(
        apiClass: Class<T>,
        dataClass: Class<DATA>,
        index: String,
        apiCall: ((api: T) -> Single<DATA>)
    ): Single<DATA> = makeCacheableApiCallForResponse(apiClass, dataClass, index, { it }, { it }, apiCall)

    fun <DATA : Any> cleanFromCache(dataClass: Class<DATA>, index: String) =
        cacheProvider.clear(dataClass, index)

}