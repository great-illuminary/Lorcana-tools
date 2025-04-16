package eu.codlab.lorcana.blipya.licenses.file

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class LicenseFile(
    val metadata: LicenseMetadataHeader? = null,
    val libraries: List<LicenseProject>
) {
    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
        }

        fun fromContent(content: String): LicenseFile = json.decodeFromString(content)
    }
}

@Serializable
data class LicenseMetadataHeader(
    val generated: String
)
