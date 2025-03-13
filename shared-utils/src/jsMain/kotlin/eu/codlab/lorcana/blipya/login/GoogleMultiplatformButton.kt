package eu.codlab.lorcana.blipya.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.viewmodel.rememberViewModel

@Composable
actual fun GoogleMultiplatformButton(
    modifier: Modifier,
    onGoogleAuthentIdToken: (Result<String>) -> Unit
) {
    val model = rememberViewModel { GoogleAuthenticationModel() }
    val provider = LocalRequestForUrlToOpen.current
    val uriHandler = LocalUriHandler.current

    GoogleSignInButton(
        modifier = Modifier.loginButton(),
        fontSize = AppSizes.login.fontSize
    ) {
        model.sendRequestForUrlToOpen(provider, uriHandler, OAuthProvider.Google)
    }
}
