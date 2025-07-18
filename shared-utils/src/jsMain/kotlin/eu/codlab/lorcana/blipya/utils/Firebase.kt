package eu.codlab.lorcana.blipya.utils

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.analytics.analytics
import dev.gitlive.firebase.initialize
import eu.codlab.blipya.config.FirebaseConfig

actual object Firebase {
    actual fun initialize() {
        val options = FirebaseOptions(
            apiKey = FirebaseConfig.apiKey,
            authDomain = FirebaseConfig.authDomain,
            projectId = FirebaseConfig.projectId,
            storageBucket = FirebaseConfig.storageBucket,
            gcmSenderId = FirebaseConfig.gcmSenderId,
            applicationId = FirebaseConfig.applicationId,
            gaTrackingId = FirebaseConfig.gaTrackingId
        )

        Firebase.initialize(null, options)
        Firebase.analytics.setAnalyticsCollectionEnabled(true)
    }

    actual fun logEvent(name: String, parameters: Map<String, Any>?) =
        Firebase.analytics.logEvent(name, parameters)
}
