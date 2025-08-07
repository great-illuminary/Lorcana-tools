package eu.codlab.blipya.config

import eu.codlab.blipya.utils.kmp.buildconfig.BuildKonfig

object FirebaseConfig {
    val apiKey = BuildKonfig.apiKey
    val authDomain = BuildKonfig.authDomain
    val projectId = BuildKonfig.projectId
    val storageBucket = BuildKonfig.storageBucket
    val gcmSenderId = BuildKonfig.gcmSenderId
    val applicationId = BuildKonfig.applicationId
    val gaTrackingId = BuildKonfig.gaTrackingId
}
