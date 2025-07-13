package eu.codlab.navigation

expect object Navigation {
    fun originalPath(): String

    fun setPath(path: String)
}
