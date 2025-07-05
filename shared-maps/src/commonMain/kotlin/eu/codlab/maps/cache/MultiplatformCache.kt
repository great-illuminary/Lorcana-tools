package eu.codlab.maps.cache

import kotlinx.coroutines.CoroutineScope
import kotlinx.io.Buffer
import ovh.plrapps.mapcompose.utils.IODispatcher

expect class MultiplatformCache(
    scope: CoroutineScope = CoroutineScope(IODispatcher),
    cacheDirName: String,
    download: suspend (x: Int, y: Int, z: Int) -> ByteArray
) {
    suspend fun initialize()

    fun cacheTile(x: Int, y: Int, z: Int)

    suspend fun getCached(x: Int, y: Int, z: Int): Buffer?
}