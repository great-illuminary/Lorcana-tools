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

        println(configuration)
    }

    suspend fun save(decks: List<SavedDeckModel>) {
        configuration = configuration.copy(decks = decks)
        val string = json.encodeToString(ConfigurationModel.serializer(), configuration)
        println("model -> ${configurationFolder.absolutePath} -> $string")
        configurationFolder.write(string)
    }
}
