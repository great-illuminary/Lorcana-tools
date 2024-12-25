package eu.codlab.lorcana.blipya.utils

import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import eu.codlab.blipya.buildconfig.BuildKonfig

actual object AuthentInit {
    actual fun initialize() {
        GoogleAuthProvider.create(
            credentials = GoogleAuthCredentials(
                serverId = BuildKonfig.googleAuthServerId
            )
        )
    }
}
