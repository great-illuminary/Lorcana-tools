package eu.codlab.lorcana.blipya.licenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.compose.widgets.TextSubtitle
import eu.codlab.compose.widgets.systemBackground
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.licenses.file.LicenseProject

@Suppress("MagicNumber")
@Composable
fun LicenseItem(license: LicenseProject) {
    val licenses = license.licenses.map { it }.filter {
        it.isNotEmpty()
    }.joinToString(separator = ", ")

    Column(modifier = Modifier.fillMaxWidth()) {
        TextSubtitle(
            text = license.name
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.6f)
        ) {
            TextNormal(
                text = "version ${license.artifactVersion}"
            )
            TextNormal(
                text = licenses
            )
            TextNormal(
                text = license.description ?: "",
                textAlign = TextAlign.Justify
            )
        }
    }
}

internal val fakeLicense = LicenseProject(
    name = "test",
    artifactVersion = "10",
    licenses = listOf(
        "MIT",
        "Apache"
    ),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
            "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis " +
            "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis " +
            "aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat " +
            "nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui " +
            "officia deserunt mollit anim id est laborum."
)

@HotPreview(widthDp = 400, heightDp = 600, darkMode = true)
@HotPreview(widthDp = 400, heightDp = 600, darkMode = false)
@Composable
fun LicenseItemPreview() {
    HotPreviewApp {
        Column(modifier = Modifier.systemBackground()) {
            LicenseItem(
                license = fakeLicense
            )
        }
    }
}
