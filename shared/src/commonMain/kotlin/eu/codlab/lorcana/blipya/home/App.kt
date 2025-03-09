package eu.codlab.lorcana.blipya.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import eu.codlab.lorcana.blipya.theme.ApplicationTheme
import eu.codlab.lorcana.blipya.theme.FontSizes
import eu.codlab.lorcana.blipya.theme.createFontSizes
import eu.codlab.lorcana.blipya.utils.LocalFrame
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.LocalWindowProvider
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.lorcana.blipya.widgets.popup.PopupConfirmCompose
import eu.codlab.lorcana.blipya.widgets.popup.PopupLocalModel
import eu.codlab.viewmodel.effects.LifecycleEffect

var staticModel: AppModel? = null

val LocalApp = compositionLocalOf<AppModel> { error("No StaticModel, check LocalApp") }
val LocalIsPreview = compositionLocalOf { false }
val LocalConfirmPopup = compositionLocalOf<PopupLocalModel> { error("No LocalPopup") }
val LocalFontSizes = compositionLocalOf<FontSizes> { error("No LocalFontSizes") }

@Composable
fun App(
    isDarkTheme: Boolean,
    onBackPressed: AppBackPressProvider = AppBackPressProvider()
) {
    LifecycleEffect {
        println("Having a new lifecycle state $it")
    }

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
        ApplicationTheme(isDarkTheme, content)
    }
}
