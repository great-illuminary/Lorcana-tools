package eu.codlab.lorcana.blipya.save

import eu.codlab.files.VirtualFile
import eu.codlab.lorcana.blipya.utils.readStringIfExists
import eu.codlab.lorcana.blipya.utils.write
import kotlinx.serialization.json.Json

class ConfigurationLoader(
    private val root: VirtualFile
) {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

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

    suspend fun save(decks: List<SavedDeckModel>) {
        configuration = configuration.copy(decks = decks)
        save()
    }

    suspend fun saveRavensburgerPlayHub(username: String, id: Long) = RavensburgerPlayHubUser(
        username,
        id
    ).also { rphUser ->
        configuration = configuration.copy(
            rphUser = rphUser
        )

        save()
    }

    suspend fun save(
        authenticationToken: String,
        expiresAtMilliSeconds: Long
    ) = SavedAuthentication(authenticationToken, expiresAtMilliSeconds).also { obj ->
        configuration = configuration.copy(
            authentication = obj
        )
        save()
    }

    suspend fun disconnect() {
        configuration = configuration.copy(
            authentication = null
        )
        save()
    }

    suspend fun save() {
        val string = json.encodeToString(ConfigurationModel.serializer(), configuration)
        configurationFolder.write(string)
    }
}
