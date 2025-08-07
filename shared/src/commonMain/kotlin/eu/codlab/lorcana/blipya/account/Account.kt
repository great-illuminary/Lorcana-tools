package eu.codlab.lorcana.blipya.account

import eu.codlab.http.Configuration
import eu.codlab.http.createClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.isSuccess

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
