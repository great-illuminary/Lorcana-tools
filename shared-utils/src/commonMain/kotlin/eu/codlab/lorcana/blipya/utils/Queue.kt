package eu.codlab.lorcana.blipya.utils

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class Queue(private val scope: CoroutineScope) {
    private val internalQueue = Channel<Job>(Channel.UNLIMITED)

    init {
        scope.launch(Dispatchers.Default) {
            for (job in internalQueue) {
                try {
                    job.join()
                } catch (ignored: Throwable) {
                    // ignored
                }
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    suspend fun enqueue(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) = suspendCoroutine {
        post(context) {
            try {
                block()

                it.resume(Unit)
            } catch (err: Throwable) {
                it.resumeWithException(err)
            }
        }
    }

    fun post(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) = internalQueue.trySend(
        scope.launch(
            context,
            CoroutineStart.LAZY,
            block
        )
    )

    fun cancel() {
        internalQueue.cancel()
        scope.cancel()
    }
}
