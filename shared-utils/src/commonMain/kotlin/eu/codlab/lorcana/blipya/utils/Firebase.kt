package eu.codlab.lorcana.blipya.utils

expect object Firebase {
    fun initialize()

    fun logEvent(name: String, parameters: Map<String, Any>? = null)
}
