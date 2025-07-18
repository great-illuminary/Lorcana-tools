package eu.codlab.lorcana.blipya.utils

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics
import dev.gitlive.firebase.initialize
import eu.codlab.files.AndroidContext

actual object Firebase {
    actual fun initialize() {
        Firebase.initialize(AndroidContext)
        Firebase.analytics.setAnalyticsCollectionEnabled(true)
    }

    actual fun logEvent(name: String, parameters: Map<String, Any>?) =
        Firebase.analytics.logEvent(name, parameters)
}
