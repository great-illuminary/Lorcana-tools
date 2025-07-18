package eu.codlab.lorcana.blipya.utils

actual object Firebase {
    actual fun initialize() {
        // nothing
    }

    actual fun logEvent(name: String, parameters: Map<String, Any>?) = Unit
}
