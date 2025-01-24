package eu.codlab.lorcana.blipya.login

import io.ktor.util.decodeBase64String
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class OAuthToken(val originalToken: String) {
    private val token = originalToken.split(".")[1]
    private val json = Json

    val firebaseToken: FirebaseToken

    init {
        val decoded = token.decodeBase64String()
        firebaseToken = json.decodeFromString(decoded)
    }
}

@Serializable
data class FirebaseToken(
    val iss: String,
    val name: String = "",
    val user_id: String = "",
    val email: String = "",
    val sign_in_provider: String = ""
)