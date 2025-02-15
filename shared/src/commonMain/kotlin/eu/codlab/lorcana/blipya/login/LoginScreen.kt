package eu.codlab.lorcana.blipya.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.theme.AppSizes

@Composable
fun LoginScreen(
    modifier: Modifier,
    appModel: AppModel
) {
    val states by appModel.states.collectAsState()
    val account = states.authentication

    println("account info -> $account")
    Column(
        modifier.padding(AppSizes.paddings.default),
        verticalArrangement = Arrangement.spacedBy(
            AppSizes.paddings.default
        )
    ) {
        if (null != account) {
            TextNormal(
                "Logged in with ${account.token}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            return@Column
        }

        AppleMultiplatformButton(Modifier.loginButton()) { result ->
            result.getOrNull()?.let { appModel.login(it) }
        }

        GoogleMultiplatformButton(Modifier.loginButton()) { result ->
            result.getOrNull()?.let { appModel.login(it) }
        }
    }
}
