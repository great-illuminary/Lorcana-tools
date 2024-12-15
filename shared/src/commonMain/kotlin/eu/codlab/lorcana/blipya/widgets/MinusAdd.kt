package eu.codlab.lorcana.blipya.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import eu.codlab.compose.widgets.TextNormal

@Composable
fun MinusAdd(onMinus: () -> Unit, onPlus: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ElevatedButton(
            onClick = onMinus,
            content = {
                TextNormal("-")
            }
        )

        ElevatedButton(
            onClick = onPlus,
            content = {
                TextNormal("+")
            }
        )
    }
}