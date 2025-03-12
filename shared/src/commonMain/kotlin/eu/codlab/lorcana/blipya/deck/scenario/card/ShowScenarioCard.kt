package eu.codlab.lorcana.blipya.deck.scenario.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.ionspin.kotlin.bignum.integer.Quadruple
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.card_show_amount
import eu.codlab.blipya.res.card_show_card_name
import eu.codlab.blipya.res.card_show_max
import eu.codlab.blipya.res.card_show_min
import eu.codlab.compose.widgets.CustomOutlinedEditText
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.scenario.edit.EditScenarioModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.asLongOrNull
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.utils.rememberInputSize
import eu.codlab.lorcana.math.ExpectedCard
import eu.codlab.viewmodel.rememberViewModel

@Suppress("LongMethod")
@Composable
fun ShowScenarioCard(
    modifier: Modifier,
    model: EditScenarioModel,
    deck: DeckModel,
    holder: ExpectedCard
) {
    var name by remember { mutableStateOf(TextFieldValue(holder.name)) }
    var amount by remember { mutableStateOf(TextFieldValue("${holder.amount}")) }
    var min by remember { mutableStateOf(TextFieldValue("${holder.min}")) }
    var max by remember { mutableStateOf(TextFieldValue("${holder.max}")) }

    val validationModel = rememberViewModel { ShowScenarioCardModel(deck, holder) }
    val validationState by validationModel.states.collectAsState()

    val maxWidth = rememberInputSize()

    val items: List<Quadruple<TextFieldValue, String, Boolean, (TextFieldValue) -> Unit>> = listOf(
        Quadruple(
            amount,
            Res.string.card_show_amount.localized(),
            validationState.amountValid
        ) {
            validationModel.update(
                it.asLongOrNull() ?: 0,
                holder.min,
                holder.max
            )
            model.updateScenario(
                holder.id,
                it.asLongOrNull() ?: 0,
                holder.min,
                holder.max
            )
            amount = it
        },
        Quadruple(
            min,
            Res.string.card_show_min.localized(),
            validationState.minValid
        ) {
            validationModel.update(
                holder.amount,
                it.asLongOrNull() ?: 0,
                holder.max
            )
            model.updateScenario(
                holder.id,
                holder.amount,
                it.asLongOrNull() ?: 0,
                holder.max
            )
            min = it
        },
        Quadruple(
            max,
            Res.string.card_show_max.localized(),
            validationState.maxValid
        ) {
            validationModel.update(
                holder.amount,
                holder.min,
                it.asLongOrNull() ?: 0
            )
            model.updateScenario(
                holder.id,
                holder.amount,
                holder.min,
                it.asLongOrNull() ?: 0
            )
            max = it
        }
    )

    Column {
        CustomOutlinedEditText(
            modifier = Modifier.width(150.dp),
            value = name,
            onValueChanged = {
                name = it
                model.updateScenario(holder.id, name.text)
            },
            label = {
                TextNormal(Res.string.card_show_card_name.localized())
            }
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier,
            horizontalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
        ) {
            items.map { (value, label, valid, onValueChanged) ->
                CustomOutlinedEditText(
                    modifier = Modifier.widthIn(0.dp, maxWidth),
                    value = value,
                    isError = !valid,
                    onValueChanged = onValueChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    label = {
                        TextNormal(label)
                    }
                )
            }
        }
    }
}
