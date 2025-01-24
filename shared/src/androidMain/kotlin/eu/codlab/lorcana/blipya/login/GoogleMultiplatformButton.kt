package eu.codlab.lorcana.blipya.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.viewmodel.rememberViewModel

@Composable
actual fun GoogleMultiplatformButton(
    modifier: Modifier,
    onGoogleAuthentIdToken: (Result<String>) -> Unit
) {
    val model = rememberViewModel { GoogleAuthenticationModel() }

    // Google Sign-In Button and authentication with Firebase
    GoogleButtonUiContainerFirebase(onResult = {
        model.setResult(it, onGoogleAuthentIdToken)
    }, linkAccount = false) {
        GoogleSignInButton(
            modifier = Modifier.loginButton(),
            fontSize = AppSizes.login.fontSize
        ) { this.onClick() }
    }
}
