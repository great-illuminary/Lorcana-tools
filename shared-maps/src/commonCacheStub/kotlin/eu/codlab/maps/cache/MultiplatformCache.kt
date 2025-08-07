package eu.codlab.maps.cache

import kotlinx.coroutines.CoroutineScope
import kotlinx.io.Buffer

actual class MultiplatformCache actual constructor(
    scope: CoroutineScope,
    cacheDirName: String,
    download: suspend (x: Int, y: Int, z: Int) -> ByteArray
) {
    actual suspend fun initialize() {
        // nothing
    }

    actual fun cacheTile(x: Int, y: Int, z: Int) {
        // nothing
    }

    actual suspend fun getCached(x: Int, y: Int, z: Int): Buffer? {
        return null
    }
}
