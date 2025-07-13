package eu.codlab.navigation

import kotlinx.browser.window

actual object Navigation {
    actual fun originalPath() = window.location.pathname

    actual fun setPath(path: String) {
        window.history.pushState(null, "", path)
    }
}
