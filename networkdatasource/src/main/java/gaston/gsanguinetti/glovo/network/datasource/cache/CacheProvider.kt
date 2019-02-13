package gaston.gsanguinetti.glovo.network.datasource.cache

interface CacheProvider {
    fun <T : Any> provide(classToProvide: Class<T>, index: String): T?
    fun <T : Any> store(it: T, index: String)
    fun <T : Any> clear(classToProvide: Class<T>, index: String)
    fun invalidate()
}