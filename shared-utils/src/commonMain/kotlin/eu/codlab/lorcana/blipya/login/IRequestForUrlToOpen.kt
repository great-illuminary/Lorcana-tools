package eu.codlab.lorcana.blipya.login

interface IRequestForUrlToOpen {
    suspend fun requestForUrlToOpen(provider: String): String?
}
