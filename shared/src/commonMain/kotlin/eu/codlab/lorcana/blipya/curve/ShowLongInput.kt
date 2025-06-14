package eu.codlab.lorcana.blipya.curve

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import eu.codlab.compose.widgets.CustomOutlinedEditText
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground

@Composable
fun ShowLongInput(
    modifier: Modifier,
    placeHolder: String,
    initialValue: (CurveInformationModelState) -> Long,
    model: CurveInformationModel,
    enablePlaceholder: String = "",
    isEnabled: Boolean = true,
    canBeDisabled: Boolean = false,
    onEnabled: (Boolean) -> Unit = {},
    onValue: (TextFieldValue) -> Unit
) {
    val color = defaultCardBackground()
    val state by model.states.collectAsState()
    val initialValue = initialValue(state)
    var value by remember { mutableStateOf(TextFieldValue("$initialValue")) }

    DefaultCard(
        modifier = modifier,
        backgroundColor = color
    ) {
        Column(
            Modifier.padding(AppSizes.paddings.default).background(color),
            verticalArrangement = Arrangement.spacedBy(AppSizes.paddings.default)
        ) {
            if (canBeDisabled) {
                Row(
                    modifier = Modifier.clickable {
                        onEnabled(!isEnabled)
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextNormal(enablePlaceholder)

                    Checkbox(
                        checked = isEnabled,
                        onCheckedChange = { onEnabled(it) }
                    )
                }
            }
            CustomOutlinedEditText(
                modifier = Modifier.width(200.dp),
                enabled = isEnabled,
                value = value,
                onValueChanged = {
                    value = it
                    onValue(it)
                },
                label = {
                    TextNormal(placeHolder)
                }
            )
        }
    }
}
