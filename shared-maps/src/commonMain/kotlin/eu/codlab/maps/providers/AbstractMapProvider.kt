package eu.codlab.maps.providers

import eu.codlab.files.VirtualFile
import eu.codlab.http.createClient
import eu.codlab.lorcana.blipya.utils.RootPath
import eu.codlab.maps.CacheManager
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.exhausted
import io.ktor.utils.io.toByteArray
import kotlinx.io.Buffer
import kotlinx.io.RawSource

abstract class AbstractMapProvider(
    cacheDirName: String,
    private val useFileCache: Boolean = false
) {
    private val cacheFolder = VirtualFile(RootPath, ".blipya/tiles")
    private val client = createClient { }
    private val cacheManager = CacheManager(cacheDirName = cacheDirName) { x, y, z ->
        getTileAsChannel(x, y, z).toByteArray()
    }

    suspend fun initialize() {
        cacheFolder.mkdirs()

        cacheManager.initialize()
    }

    protected abstract suspend fun buildUrl(row: Int, col: Int, zoomLvl: Int): String

    private suspend fun getTileAsByteArray(
        row: Int,
        col: Int,
        zoomLvl: Int,
        writeInFile: VirtualFile? = null
    ): RawSource? {
        val url = buildUrl(row, col, zoomLvl)

        val request = client.get(url)
        if (!request.status.isSuccess()) {
            println(request.bodyAsText())
            return null
        }

        return Buffer().also {
            val array = request.bodyAsChannel().toByteArray()

            writeInFile?.let { write -> write.write(array) }
            it.write(array)
        }
    }

    private suspend fun getTileAsChannel(row: Int, col: Int, zoomLvl: Int): ByteReadChannel {
        val url = buildUrl(row, col, zoomLvl)

        val request = client.get(url)
        if (!request.status.isSuccess()) {
            println(request.bodyAsText())
            throw IllegalStateException("")
        }

        val body = request.bodyAsChannel()
        body.exhausted()

        return body
    }

    @Suppress("TooGenericExceptionCaught", "ReturnCount")
    suspend fun getTile(row: Int, col: Int, zoomLvl: Int): RawSource? {
        val file = VirtualFile(cacheFolder, "${row}_${col}_$zoomLvl.jpeg")

        return try {
            if (useFileCache && file.exists()) {
                val content = file.read()
                if (content.isNotEmpty()) {
                    return Buffer().also { it.write(content) }
                }
            }
            val inCache = cacheManager.getCached(row, col, zoomLvl)

            if (null != inCache) {
                return inCache
            }

            val body = getTileAsByteArray(row, col, zoomLvl, if (useFileCache) file else null)

            if (null != body) {
                cacheManager.cacheTile(row, col, zoomLvl)
            }

            body
        } catch (_: Throwable) {
            null
        }
    }
}
