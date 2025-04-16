package eu.codlab.lorcana.blipya.licenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.viewmodel.rememberViewModel

@Suppress("TooGenericExceptionCaught", "UnusedPrivateMember")
@Composable
fun LicensesContent(
    appModel: AppModel,
    model: LicensesModel = rememberViewModel { LicensesModel() }
) {
    val state by model.states.collectAsState()

    LaunchedEffect(state.loaded) {
        if (!state.loaded) {
            model.load()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .defaultBackground()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            if (!state.loaded) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier
                ) {
                    items(state.licenses.size) {
                        LicenseItem(state.licenses[it])

                        HorizontalDivider(
                            modifier = Modifier.height(16.dp),
                            color = Color.Transparent
                        )
                    }
                }
            }
        }
    }
}

private val fakeModel = LicensesModel.fake(
    loaded = true,
    licenses = listOf(
        fakeLicense,
        fakeLicense,
        fakeLicense,
        fakeLicense
    )
)

@HotPreview(widthDp = 400, heightDp = 600, darkMode = true)
@HotPreview(widthDp = 400, heightDp = 600, darkMode = false)
@Composable
private fun LicensesContentPreview() {
    HotPreviewApp {
        LicensesContent(
            AppModel("", ""),
            fakeModel
        )
    }
}
