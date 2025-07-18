package eu.codlab.lorcana.blipya.utils

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics

actual object Firebase {
    actual fun initialize() {
        Firebase.analytics.setAnalyticsCollectionEnabled(true)
    }

    actual fun logEvent(name: String, parameters: Map<String, Any>?) =
        Firebase.analytics.logEvent(name, parameters)
}
