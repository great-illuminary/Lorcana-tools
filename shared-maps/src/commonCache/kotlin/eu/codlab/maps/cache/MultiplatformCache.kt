package eu.codlab.maps.cache

import com.mayakapps.kache.FileKache
import com.mayakapps.kache.KacheStrategy
import eu.codlab.files.VirtualFile
import eu.codlab.lorcana.blipya.utils.Queue
import eu.codlab.lorcana.blipya.utils.RootPath
import eu.codlab.maps.Triple
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.io.Buffer
import ovh.plrapps.mapcompose.utils.IODispatcher

actual class MultiplatformCache actual constructor(
    private val scope: CoroutineScope,
    private val cacheDirName: String,
    private val download: suspend (x: Int, y: Int, z: Int) -> ByteArray
) {
    private val queue = Queue(CoroutineScope(IODispatcher))
    private val mutable = mutableListOf<Triple>()
    private val listAccessQueue = Queue(CoroutineScope(IODispatcher))
    private val cached: MutableSet<Triple> = mutableSetOf()

    private lateinit var cache: FileKache

    actual suspend fun initialize() {
        val cacheDir = VirtualFile(
            RootPath,
            "cache/$cacheDirName"
        )
        cacheDir.mkdirs()
        val files = VirtualFile(cacheDir, "files")
        files.mkdirs()

        cache = com.mayakapps.kache.FileKache(
            directory = cacheDir.absolutePath,
            maxSize = 500 * 1024 * 1024
        ) {
            // Other optional configurations
            strategy = KacheStrategy.MRU
        }
    }

    actual fun cacheTile(x: Int, y: Int, z: Int) {
        ensureSize()

        listAccessQueue.post {
            mutable.add(Triple(x, y, z))

            queue.post {
                attemptToCache()
            }
        }
    }

    private fun ensureSize() {
        listAccessQueue.post {
            while (mutable.size > 20) {
                mutable.removeAt(0)
            }
        }
    }

    actual suspend fun getCached(x: Int, y: Int, z: Int): Buffer? {
        val path = cache.getIfAvailable(Triple(x, y, z).toKey()) ?: return null

        val file = VirtualFile(path)
        val buffer = Buffer()

        buffer.write(file.read())

        return buffer
    }


    private suspend fun hasItems(): Boolean {
        return suspendCoroutine { result ->
            listAccessQueue.post {
                result.resume(mutable.size > 0)
            }
        }
    }

    private suspend fun getListQueue(): Triple? {
        return suspendCoroutine { result ->
            listAccessQueue.post {
                try {
                    val last = mutable.removeLast()
                    result.resume(last)
                } catch (err: Throwable) {
                    result.resume(null)
                }
            }
        }
    }

    private fun attemptToCache() {
        queue.post {
            val head = getListQueue() ?: return@post

            if (null != cache.get(head.toKey())) {
                return@post
            }

            cache.put(head.toKey()) { fileName ->
                try {
                    val download = download(head.x, head.y, head.z)

                    val file = VirtualFile(fileName)
                    file.write(download)

                    true
                } catch (err: Throwable) {
                    err.printStackTrace()
                    false
                }
            }

            if (!hasItems()) return@post

            listAccessQueue.post {
                cached.add(head)
            }

            scope.async {
                ensureSize()
                attemptToCache()
            }
        }
    }
}