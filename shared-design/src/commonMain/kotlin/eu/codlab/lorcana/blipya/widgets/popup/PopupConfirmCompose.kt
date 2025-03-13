package eu.codlab.lorcana.blipya.widgets.popup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import eu.codlab.lorcana.blipya.local.LocalConfirmPopup
import eu.codlab.lorcana.blipya.widgets.PopupConfirm

@Composable
fun PopupConfirmCompose() {
    val model = LocalConfirmPopup.current
    val state by model.states.collectAsState()

    val show = state.invocations.firstOrNull()
        ?: return

    val dismiss: () -> Unit = {
        show.let {
            model.dismiss(show.uuid)
        }
    }

    // if we donhave nothing to show, this may
    // be removing "too quickly" the view
    // possible improvement -> always show if show!!
    // and test changing the values live
    PopupConfirm(
        show = true,
        title = show.title,
        text = show.text,
        showCancel = null != show.onDismiss,
        onConfirm = {
            dismiss()
            show.onConfirm()
        },
        onDismiss = {
            dismiss()
            show.onDismiss?.let { it() }
        }
    )
}
