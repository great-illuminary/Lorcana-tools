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
import androidx.compose.ui.text.intl.Locale
import eu.codlab.lorcana.blipya.theme.ApplicationTheme
import eu.codlab.lorcana.blipya.theme.FontSizes
import eu.codlab.lorcana.blipya.theme.createFontSizes
import eu.codlab.lorcana.blipya.utils.LocalWindowProvider
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.lorcana.blipya.widgets.popup.PopupConfirmCompose
import eu.codlab.lorcana.blipya.widgets.popup.PopupLocalModel
import eu.codlab.viewmodel.effects.LifecycleEffect
import eu.codlab.viewmodel.rememberViewModel

val staticModel = AppModel(
    "",
    ""
)

val LocalApp = compositionLocalOf { staticModel }
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

    InternalApp(
        modifier = Modifier.fillMaxSize(),
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

@Composable
fun PreviewApp(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onBackPressed: AppBackPressProvider = AppBackPressProvider(),
    content: @Composable () -> Unit
) {
    InternalApp(
        modifier = modifier,
        isDarkTheme,
        onBackPressed
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .defaultBackground()
        ) {
            content()
        }
    }
}

@Composable
private fun InternalApp(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onBackPressed: AppBackPressProvider = AppBackPressProvider(),
    content: @Composable () -> Unit
) {
    val confirmPopup by remember { mutableStateOf(PopupLocalModel()) }
    val fontSizes = createFontSizes()

    val model = staticModel // rememberViewModel { staticModel }
    //val locale = Locale("fr_fr")

    LaunchedEffect(onBackPressed) {
        model.onBackPressed = onBackPressed
    }

    CompositionLocalProvider(
        LocalConfirmPopup provides confirmPopup,
        LocalApp provides model,
        LocalFontSizes provides fontSizes
    ) {
        ApplicationTheme(
            darkTheme = isDarkTheme
        ) {
            LocalWindowProvider(modifier) {
                content()
            }
        }
    }
}
