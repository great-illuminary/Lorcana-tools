package eu.codlab.lorcana.blipya.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.viewmodel.rememberViewModel

@Composable
fun AppleMultiplatformButtonOAuthOnly(
    modifier: Modifier
) {
    val model = rememberViewModel { GoogleAuthenticationModel() }
    val app = LocalApp.current
    val uriHandler = LocalUriHandler.current

    AppleSignInButton(
        modifier = modifier.fillMaxWidth().height(40.dp)
    ) {
        model.sendRequestForUrlToOpen(app, uriHandler, OAuthProvider.Apple)
    }
}