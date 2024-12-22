package eu.codlab.lorcana.blipya.login

import com.mmk.kmpauth.google.GoogleUser
import dev.gitlive.firebase.auth.FirebaseUser
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch

data class GoogleAuthenticationModelState(
    val idToken: String? = null
)

@Suppress("TooManyFunctions")
class GoogleAuthenticationModel() :
    StateViewModel<GoogleAuthenticationModelState>(
        GoogleAuthenticationModelState()
    ) {

    fun setResult(
        result: GoogleUser?,
        onGoogleAuthentIdToken: (Result<String>) -> Unit
    ) = launch {
        try {
            // val id = Firebase.auth.currentUser?.getIdToken(false)!!
            val id = result?.idToken!!
            //val id = result?.accessToken!!

            updateState {
                copy(idToken = id)
            }

            onGoogleAuthentIdToken(Result.success(id))
        } catch (err: Throwable) {
            onGoogleAuthentIdToken(Result.failure(err))
        }
    }

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
}
