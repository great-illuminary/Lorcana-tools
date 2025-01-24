package eu.codlab.lorcana.blipya.login

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mmk.kmpauth.uihelper.apple.AppleButtonMode
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.viewmodel.rememberViewModel

@Composable
fun AppleMultiplatformButtonOAuthOnly(
    modifier: Modifier
) {
    val model = rememberViewModel { GoogleAuthenticationModel() }
    val app = LocalApp.current
    val uriHandler = LocalUriHandler.current

    val buttonMode = if (LocalDarkTheme.current) {
        AppleButtonMode.White
    } else {
        AppleButtonMode.Black
    }

    AppleSignInButton(
        mode = buttonMode,
        modifier = modifier.loginButton().height(100.dp),
    ) {
        model.sendRequestForUrlToOpen(app, uriHandler, OAuthProvider.Apple)
    }
}