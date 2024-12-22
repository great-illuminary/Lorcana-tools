package eu.codlab.lorcana.blipya.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.home.AppModel

@Composable
fun LoginScreen(
    modifier: Modifier,
    appModel: AppModel
) {
    val states by appModel.states.collectAsState()
    val account = states.authentication

    println("account info -> $account")
    Column(modifier) {
        if (null != account) {
            TextNormal(
                "Logged in with ${account.token}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            return@Column
        }

        AppleMultiplatformButton(Modifier.height(20.dp)) { result ->
            result.getOrNull()?.let { appModel.login(it) }
        }

        GoogleMultiplatformButton(Modifier.height(20.dp)) { result ->
            result.getOrNull()?.let { appModel.login(it) }
        }
    }
}
