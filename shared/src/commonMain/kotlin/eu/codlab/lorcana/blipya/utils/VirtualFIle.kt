package eu.codlab.lorcana.blipya.utils

import eu.codlab.files.VirtualFile

expect suspend fun VirtualFile.write(text: String)

expect suspend fun VirtualFile.readStringIfExists(): String?
