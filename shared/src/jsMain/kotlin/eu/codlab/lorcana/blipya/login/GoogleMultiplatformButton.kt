package eu.codlab.lorcana.blipya.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.viewmodel.rememberViewModel

@Composable
actual fun GoogleMultiplatformButton(
    modifier: Modifier,
    onGoogleAuthentIdToken: (Result<String>) -> Unit
) {
    val model = rememberViewModel { GoogleAuthenticationModel() }
    val app = LocalApp.current
    val uriHandler = LocalUriHandler.current

    GoogleSignInButton(
        modifier = Modifier.fillMaxWidth().height(25.dp),
        fontSize = 15.sp
    ) {
        model.sendRequestForUrlToOpen(app, uriHandler)
    }
}
