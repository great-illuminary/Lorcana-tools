package eu.codlab.lorcana.blipya.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.codlab.compose.widgets.spacers.TopSpacer
import eu.codlab.lorcana.blipya.local.LocalFontSizes
import eu.codlab.visio.design.appbar.AppBarStateProvider
import eu.codlab.visio.design.appbar.AppBarUiState
import eu.codlab.lorcana.blipya.theme.LocalApplicationColorTheme

@Composable
fun <T : AppBarUiState> TopAppBarExtended(
    title: String,
    topSpacer: Boolean,
    canGoBack: Boolean,
    isScreenExpanded: Boolean,
    appModel: AppBarStateProvider<T>,
    onNavigationClick: () -> Unit
) {
    val colors = LocalApplicationColorTheme.current
    val background = colors.background.topAppBar
    val tint = colors.background.topAppBarText

    val modelState by appModel.states.collectAsState()
    val appBarState = modelState.appBarState

    val menu = if (canGoBack) {
        Icons.AutoMirrored.Filled.ArrowBack
    } else if (!isScreenExpanded) {
        Icons.Filled.Menu
    } else {
        null
    }

    Column(
        modifier = Modifier.fillMaxWidth().background(background)
    ) {
        if (topSpacer) {
            TopSpacer(background)
        }

        TopAppBar(
            elevation = 4.dp,
            title = {
                Text(
                    fontSize = LocalFontSizes.current.actionBar.title,
                    text = title,
                    color = tint
                )
            },
            backgroundColor = background,
            navigationIcon = if (null != menu) {
                {
                    IconButton(
                        onClick = onNavigationClick
                    ) {
                        Icon(
                            menu,
                            null,
                            tint = tint
                        )
                    }
                }
            } else {
                null
            },
            actions = {
                appBarState.actions?.forEach {
                    it.Draw(this, tint)
                }
            }
        )
    }
}
