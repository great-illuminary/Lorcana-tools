package eu.codlab.lorcana.blipya.account

import eu.codlab.http.createClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable

class Account {
    private val client = createClient { }
    private val backend = "https://app.blipya.com"

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
}

@Serializable
data class LoginCredentials(
    val token: String,
    val expiresIn: Long
)
