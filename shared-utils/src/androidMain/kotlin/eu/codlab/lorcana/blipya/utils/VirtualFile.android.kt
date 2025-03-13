package eu.codlab.lorcana.blipya.utils

import eu.codlab.files.VirtualFile

actual suspend fun VirtualFile.write(text: String) {
    write(text.toByteArray())
}

actual suspend fun VirtualFile.readStringIfExists() = if (exists()) {
    readString()
} else {
    null
}
