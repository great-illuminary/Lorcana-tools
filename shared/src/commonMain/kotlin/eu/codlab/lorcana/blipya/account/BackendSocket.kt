package eu.codlab.lorcana.blipya.account

import eu.codlab.dispatchers.DefaultDispatcher
import eu.codlab.lorcana.blipya.home.SocketMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.wss
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlin.time.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

class BackendSocket(
    private val backend: String,
    private val client: HttpClient
) {
    private val context = CoroutineScope(DefaultDispatcher + SupervisorJob())
    private val incomingFlow = MutableSharedFlow<Frame.Text>()
    private var sendFlow: suspend (String) -> Unit = { /* nothing */ }
    private var connected = false
    private var awaiting = mutableListOf<String>()
    private val json = Json

    val incoming: SharedFlow<Frame.Text> = incomingFlow.asSharedFlow()

    // todo : reconnection
    init {
        context.launch {
            @Suppress("TooGenericExceptionCaught", "SwallowedException")
            try {
                client.wss("$backend/socket") {
                    try {
                        connected = true

                        sendFlow = { string ->
                            try {
                                send(Frame.Text(string))
                            } catch (err: Throwable) {
                                err.printStackTrace()
                            }
                        }

                        awaiting.forEach { emit(it) }

                        for (message in incoming) {
                            if (message is Frame.Text) {
                                incomingFlow.emit(message)
                            }
                        }
                    } catch (err: Throwable) {
                        err.printStackTrace()
                    }
                }
            } catch (_: Throwable) {
                println("managed exception in the app ! TODO reconnect")
            }
        }
    }

    fun emit(text: String) {
        if (!connected) {
            awaiting.add(text)
            return
        }

        context.launch { sendFlow(text) }
    }

    fun <T> emit(obj: T, serializer: SerializationStrategy<T>) =
        emit(json.encodeToString(serializer, obj))

    @Suppress("SwallowedException", "TooGenericExceptionCaught")
    suspend fun <T> waitForSocket(
        id: Long,
        seconds: Duration,
        serializer: KSerializer<T>
    ): T? {
        var obj: SocketMessage<T>? = null
        try {
            withTimeoutOrNull<T?>(seconds) {
                incoming.collect {
                    try {
                        val internalObj = json.decodeFromString(
                            SocketMessage.serializer(serializer),
                            it.readText()
                        )

                        if (internalObj.id == id) {
                            obj = internalObj
                            cancel()
                        }
                    } catch (err: Throwable) {
                        // nothing
                    }
                }
            }
        } catch (err: Throwable) {
            // nothing
        }

        return obj?.message
    }
}
