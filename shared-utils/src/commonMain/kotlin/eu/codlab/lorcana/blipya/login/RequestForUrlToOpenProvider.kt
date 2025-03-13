package eu.codlab.lorcana.blipya.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

val LocalRequestForUrlToOpen: ProvidableCompositionLocal<IRequestForUrlToOpen> =
    compositionLocalOf { error("RequestForUrlToOpenProvider() not called") }

@Composable
fun RequestForUrlToOpenProvider(
    provider: IRequestForUrlToOpen,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalRequestForUrlToOpen provides provider
    ) {
        content()
    }
}
