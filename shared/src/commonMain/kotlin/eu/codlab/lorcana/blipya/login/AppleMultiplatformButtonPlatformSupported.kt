package eu.codlab.lorcana.blipya.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mmk.kmpauth.firebase.apple.AppleButtonUiContainer
import com.mmk.kmpauth.uihelper.apple.AppleButtonMode
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.viewmodel.rememberViewModel

@Composable
fun AppleMultiplatformButtonPlatformSupport(
    modifier: Modifier,
    onGoogleAuthentIdToken: (Result<String>) -> Unit
) {
    val model = rememberViewModel { GoogleAuthenticationModel() }

    val buttonMode = if (LocalDarkTheme.current) {
        AppleButtonMode.White
    } else {
        AppleButtonMode.Black
    }

    AppleButtonUiContainer(
        modifier,
        linkAccount = false,
        onResult = {
            model.setResult(it, onGoogleAuthentIdToken)
        }
    ) {
        AppleSignInButton(
            modifier = Modifier.loginButton(),
            mode = buttonMode
        ) { this.onClick() }
    }
}
