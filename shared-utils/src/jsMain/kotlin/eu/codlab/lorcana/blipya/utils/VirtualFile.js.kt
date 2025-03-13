package eu.codlab.lorcana.blipya.utils

import eu.codlab.files.VirtualFile
import kotlinx.browser.window

actual suspend fun VirtualFile.write(text: String) {
    window.localStorage.setItem(absolutePath, text)
}

actual suspend fun VirtualFile.readStringIfExists() = window.localStorage.getItem(absolutePath)
