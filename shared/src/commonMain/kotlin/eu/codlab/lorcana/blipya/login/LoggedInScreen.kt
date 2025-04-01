package eu.codlab.lorcana.blipya.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.delete_account_button
import eu.codlab.blipya.res.disconnect_button
import eu.codlab.blipya.res.you_are_connected
import eu.codlab.compose.widgets.CustomOutlinedButton
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.save.SavedAuthentication
import eu.codlab.lorcana.blipya.utils.localized

// Note : we currently won't show account information
@Suppress("UnusedPrivateMember")
@Composable
fun LoggedInScreen(
    modifier: Modifier,
    account: SavedAuthentication,
    onDisconnect: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    Column(modifier.padding(16.dp)) {
        TextNormal(
            Res.string.you_are_connected.localized(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(32.dp))

        listOf(
            Res.string.disconnect_button.localized() to onDisconnect,
            Res.string.delete_account_button.localized() to onDeleteAccount
        ).map {
            CustomOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = it.second
            ) {
                TextNormal(
                    text = it.first,
                )
            }
        }
    }
}

@Suppress("UnusedPrivateMember")
@HotPreview(widthDp = 600, heightDp = 400, darkMode = true)
@HotPreview(widthDp = 600, heightDp = 400, darkMode = false)
@Composable
private fun LoggedInScreenPreview() {
    HotPreviewApp(Modifier.fillMaxSize()) {
        LoggedInScreen(
            Modifier.fillMaxSize(),
            SavedAuthentication(
                token = "",
                expiresAtMilliseconds = 0
            ),
            onDisconnect = { /* */ },
            onDeleteAccount = { /* */ }
        )
    }
}
