package eu.codlab.lorcana.blipya.licenses

import eu.codlab.blipya.res.Res
import eu.codlab.lorcana.blipya.licenses.file.LicenseFile
import eu.codlab.lorcana.blipya.licenses.file.LicenseProject
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.viewmodel.StateViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi

data class LicenseState(
    var loaded: Boolean = false,
    var licenses: List<LicenseProject> = emptyList()
)

class LicensesModel(
    loaded: Boolean = false,
    licenses: List<LicenseProject> = emptyList()
) : StateViewModel<LicenseState>(LicenseState(loaded = loaded, licenses = licenses)) {
    companion object {
        fun fake(
            loaded: Boolean = false,
            licenses: List<LicenseProject> = emptyList()
        ) = LicensesModel(loaded, licenses)
    }

    @OptIn(ExperimentalResourceApi::class)
    fun load() = safeLaunch {
        val content = Res.readBytes("files/aboutlibraries.json").decodeToString()
        val serialized = LicenseFile.fromContent(content)
        val result = LicenseProject.fromArray(serialized.libraries)

        updateState {
            copy(loaded = true, licenses = result)
        }
    }
}
