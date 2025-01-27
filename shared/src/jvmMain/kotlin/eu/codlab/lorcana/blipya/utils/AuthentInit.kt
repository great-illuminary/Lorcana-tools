package eu.codlab.lorcana.blipya.utils

import java.awt.Desktop
import java.awt.Desktop.Action

actual object AuthentInit {
    actual fun initialize() {
        // https://twalcari.github.io/blog/url-handler-as-an-alternative-to-java-webstart/
        if (Desktop.getDesktop().isSupported(Action.APP_OPEN_URI)) {
            Desktop.getDesktop().setOpenURIHandler { println("caught uri ${it.uri}") }
        }
    }
}
