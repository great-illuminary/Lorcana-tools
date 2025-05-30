package eu.codlab.blipya.config

import eu.codlab.blipya.buildconfig.BuildKonfig

object SharedConfig {
    val googleAuthServerId = BuildKonfig.googleAuthServerId
    val sentryDsn = BuildKonfig.sentryDsn
    val version = BuildKonfig.version
}
