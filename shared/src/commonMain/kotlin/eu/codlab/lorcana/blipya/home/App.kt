package eu.codlab.lorcana.blipya.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import eu.codlab.lorcana.blipya.local.LocalConfirmPopup
import eu.codlab.lorcana.blipya.local.LocalFontSizes
import eu.codlab.lorcana.blipya.theme.ApplicationColorTheme
import eu.codlab.lorcana.blipya.theme.ApplicationTheme
import eu.codlab.lorcana.blipya.theme.createFontSizes
import eu.codlab.lorcana.blipya.utils.LocalFrame
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.LocalWindowProvider
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.lorcana.blipya.widgets.popup.PopupConfirmCompose
import eu.codlab.lorcana.blipya.widgets.popup.PopupLocalModel
import eu.codlab.navigation.NavigatorLocalProvider

var staticModel: AppModel? = null

val LocalApp = compositionLocalOf<AppModel> { error("No StaticModel, check LocalApp") }
val LocalIsPreview = compositionLocalOf { false }

@Composable
fun App(
    isDarkTheme: Boolean,
    onBackPressed: AppBackPressProvider = AppBackPressProvider()
) {
    LocalWindowProvider(Modifier.fillMaxSize()) {
        InternalApp(
            isDarkTheme,
            onBackPressed
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .defaultBackground()
            ) {
                AppContent()

                PopupConfirmCompose()
            }
        }
    }
}

@Composable
fun PreviewApp(
    modifier: Modifier = Modifier,
    windowType: WindowType = WindowType.TABLET,
    frameType: WindowType = WindowType.TABLET,
    isDarkTheme: Boolean,
    onBackPressed: AppBackPressProvider = AppBackPressProvider(),
    isPreview: Boolean = false,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalWindow provides windowType,
        LocalFrame provides frameType,
        LocalIsPreview provides isPreview
    ) {
        InternalApp(
            isDarkTheme,
            onBackPressed
        ) {
            Column(modifier.fillMaxSize().defaultBackground()) {
                content()
            }
        }
    }
}

@Composable
private fun InternalApp(
    isDarkTheme: Boolean,
    onBackPressed: AppBackPressProvider = AppBackPressProvider(),
    content: @Composable () -> Unit
) {
    val confirmPopup by remember { mutableStateOf(PopupLocalModel()) }
    val fontSizes = createFontSizes()

    if (null == staticModel) {
        staticModel = AppModel(
            "",
            ""
        )
    }
    val model = staticModel!!

    LaunchedEffect(onBackPressed) {
        model.onBackPressed = onBackPressed
    }

    CompositionLocalProvider(
        LocalConfirmPopup provides confirmPopup,
        LocalApp provides model,
        LocalFontSizes provides fontSizes
    ) {
        NavigatorLocalProvider(model) {
            ApplicationColorTheme(isDarkTheme) {
                // RequestForUrlToOpenProvider(model) {
                ApplicationTheme(isDarkTheme, content)
                // }
            }
        }
    }
}
