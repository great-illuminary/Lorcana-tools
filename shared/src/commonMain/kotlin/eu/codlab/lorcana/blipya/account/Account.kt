package eu.codlab.lorcana.blipya.account

import eu.codlab.http.Configuration
import eu.codlab.http.createClient
import eu.codlab.lorcana.blipya.home.SocketMessage
import eu.codlab.lorcana.contexts.DefaultDispatcher
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.isSuccess
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlin.time.Duration

class Account {
    private val client = createClient(Configuration(enableSocket = true)) { }
    private val backend = "https://app.blipya.com"
    private val wss = "wss://app.blipya.com"

    suspend fun login(token: String): LoginCredentials {
        val request = client.post("$backend/user") {
            header("x-token", token)
        }

        if (!request.status.isSuccess()) throw IllegalStateException("Having http ${request.status}")
        return request.body()
    }

    suspend fun checkAccount(token: String): Boolean {
        val request = client.get("$backend/user/registered/decks") {
            header("x-token", token)
        }

        return request.status.isSuccess()
    }

    fun createSocket() = BackendSocket(wss, client)
}

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
            client.webSocket("$backend/socket") {
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

@Serializable
data class LoginCredentials(
    val token: String,
    val expiresIn: Long
)
