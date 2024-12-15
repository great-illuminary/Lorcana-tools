package eu.codlab.lorcana.blipya.utils

actual val RootPath: String
    get() {
        return System.getProperty("user.home")
    }
