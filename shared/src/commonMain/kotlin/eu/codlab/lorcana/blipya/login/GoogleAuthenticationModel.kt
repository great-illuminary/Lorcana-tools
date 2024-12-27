package eu.codlab.lorcana.blipya.login

import androidx.compose.ui.platform.UriHandler
import com.mmk.kmpauth.google.GoogleUser
import dev.gitlive.firebase.auth.FirebaseUser
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch
import korlibs.time.DateTime
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds

data class GoogleAuthenticationModelState(
    val idToken: String? = null
)

class GoogleAuthenticationModel : StateViewModel<GoogleAuthenticationModelState>(
    GoogleAuthenticationModelState()
) {
    @Suppress("TooGenericExceptionCaught")
    fun setResult(
        result: GoogleUser?,
        onGoogleAuthentIdToken: (Result<String>) -> Unit
    ) = launch {
        val res = try {
            val id = result?.idToken!!

            updateState {
                copy(idToken = id)
            }

            Result.success(id)
        } catch (err: Throwable) {
            Result.failure(err)
        }

        onGoogleAuthentIdToken(res)
    }

    @Suppress("TooGenericExceptionCaught")
    fun setResult(
        result: Result<FirebaseUser?>,
        onGoogleAuthentIdToken: (Result<String>) -> Unit
    ) = launch {
        try {
            val id = result.getOrNull()?.getIdToken(false)!!

            updateState {
                copy(idToken = id)
            }

            onGoogleAuthentIdToken(Result.success(id))
        } catch (err: Throwable) {
            onGoogleAuthentIdToken(Result.failure(err))
        }
    }

    fun sendRequestForUrlToOpen(
        app: AppModel,
        uriHandler: UriHandler
    ) = launch {
        val result = app.requestForUrlToOpen("google")

        if (null == result) {
            println("Didn't receive an url !!")
            return@launch
        }

        uriHandler.openUri(result)
    }
}

