package gaston.gsanguinetti.glovo.network.datasource.cache

import android.content.Context
import wiki.depasquale.mcache.Cache

class DeviceCacheProvider(context: Context) : CacheProvider {

    init {
        Cache.with(context)
        invalidate()
    }

    override fun <T : Any> provide(classToProvide: Class<T>, index: String): T? =
        Cache.obtain(classToProvide)
            .ofIndex(index)
            .build()
            .getNow()

    override fun <T : Any> store(it: T, index: String) {
        Cache.give(it)
            .ofIndex(index)
            .build()
            .getNow()
    }

    override fun <T : Any> clear(classToProvide: Class<T>, index: String) {
        Cache.obtain(classToProvide)
            .ofIndex(index)
            .build()
            .delete()
    }

    override fun invalidate() {
        Cache.obtain(Cache::class.java).build().delete()
    }
}