package eu.codlab.lorcana.blipya.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.delete_account_done
import eu.codlab.blipya.res.delete_account_text
import eu.codlab.blipya.res.delete_account_title
import eu.codlab.blipya.res.disconnect_done
import eu.codlab.blipya.res.disconnect_text
import eu.codlab.blipya.res.disconnect_title
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.local.LocalConfirmPopup
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.localized

@Composable
fun LoginScreen(
    modifier: Modifier,
    appModel: AppModel
) {
    val states by appModel.states.collectAsState()
    val account = states.authentication

    val disconnectTitle = Res.string.disconnect_title.localized()
    val disconnectText = Res.string.disconnect_text.localized()
    val disconnectDone = Res.string.disconnect_done.localized()
    val deleteTitle = Res.string.delete_account_title.localized()
    val deleteText = Res.string.delete_account_text.localized()
    val deleteDone = Res.string.delete_account_done.localized()

    val popup = LocalConfirmPopup.current

    Column(
        modifier.padding(AppSizes.paddings.default),
        verticalArrangement = Arrangement.spacedBy(
            AppSizes.paddings.default
        )
    ) {
        if (null != account) {
            LoggedInScreen(
                Modifier.fillMaxSize(),
                account,
                onDisconnect = {
                    popup.show(
                        disconnectTitle,
                        disconnectText,
                        onDismiss = { /* nothing */ },
                        onConfirm = {
                            appModel.disconnect {
                                popup.show(disconnectTitle, disconnectDone)
                            }
                        }
                    )
                },
                onDeleteAccount = {
                    popup.show(
                        deleteTitle,
                        deleteText,
                        onDismiss = { /* nothing */ },
                        onConfirm = {
                            appModel.delete {
                                popup.show(deleteTitle, deleteDone)
                            }
                        }
                    )
                }
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
