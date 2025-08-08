package eu.codlab.lorcana.blipya.save

import eu.codlab.files.VirtualFile
import eu.codlab.lorcana.blipya.utils.Queue
import eu.codlab.lorcana.blipya.utils.readStringIfExists
import eu.codlab.lorcana.blipya.utils.write
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import ovh.plrapps.mapcompose.utils.IODispatcher

class ConfigurationLoader(
    private val root: VirtualFile
) {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    private val queue = Queue(CoroutineScope(IODispatcher))
    private val configurationFolder = VirtualFile(root, "configuration.json")
    lateinit var configuration: ConfigurationModel
        private set

    suspend fun init() {
        if (::configuration.isInitialized) {
            return
        }

        root.mkdirs()

        configuration = configurationFolder.readStringIfExists()?.let {
            json.decodeFromString(it)
        } ?: ConfigurationModel()
    }

    suspend fun save(decks: List<SavedDeckModel>) = save { it.copy(decks = decks) }

    suspend fun saveRavensburgerPlayHub(username: String, id: Long) =
        RavensburgerPlayHubUser(
            username,
            id
        ).also { rphUser -> save { it.copy(rphUser = rphUser) } }

    suspend fun save(
        authenticationToken: String,
        expiresAtMilliSeconds: Long
    ) = SavedAuthentication(authenticationToken, expiresAtMilliSeconds).also { obj ->
        save { it.copy(authentication = obj) }
    }

    suspend fun disconnect() = save { it.copy(authentication = null) }

    private suspend fun save(mutable: (ConfigurationModel) -> ConfigurationModel) = queue.enqueue {
        mutable(configuration).let {
            val string = json.encodeToString(ConfigurationModel.serializer(), it)
            configurationFolder.write(string)

            configuration = it
        }
    }
}
