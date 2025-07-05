package eu.codlab.maps

import eu.codlab.maps.cache.MultiplatformCache
import kotlinx.coroutines.CoroutineScope
import ovh.plrapps.mapcompose.utils.IODispatcher

class CacheManager(
    scope: CoroutineScope = CoroutineScope(IODispatcher),
    cacheDirName: String,
    download: suspend (x: Int, y: Int, z: Int) -> ByteArray
) {
    private val cache = MultiplatformCache(scope, cacheDirName, download)

    suspend fun initialize() {
        cache.initialize()
    }

    fun cacheTile(x: Int, y: Int, z: Int) = cache.cacheTile(x, y, z)

    suspend fun getCached(x: Int, y: Int, z: Int) = cache.getCached(x, y, z)
}