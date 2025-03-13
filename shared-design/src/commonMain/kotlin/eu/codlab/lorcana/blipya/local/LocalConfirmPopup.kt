package eu.codlab.lorcana.blipya.local

import androidx.compose.runtime.compositionLocalOf
import eu.codlab.lorcana.blipya.widgets.popup.PopupLocalModel

val LocalConfirmPopup = compositionLocalOf<PopupLocalModel> { error("No LocalPopup") }
