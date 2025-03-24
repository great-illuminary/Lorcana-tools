package eu.codlab.lorcana.blipya.home.scaffold

import androidx.compose.foundation.layout.Column
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.widgets.BottomSpacer

@Composable
fun FloatingActionButtonWrapper() {
    val state by LocalApp.current.states.collectAsState()

    Column {
        state.floatingActionButtonState?.let {
            FloatingActionButton(
                onClick = it.action
            ) {
                Icon(
                    imageVector = it.icon,
                    contentDescription = it.contentDescription
                )
            }

            BottomSpacer()
        }
    }
}
