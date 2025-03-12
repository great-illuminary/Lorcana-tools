package eu.codlab.lorcana.blipya.deck.mulligan.card

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
import eu.codlab.compose.widgets.CustomOutlinedEditText
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.mulligan.edit.EditMulliganModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.asLongOrNull
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.utils.rememberInputSize
import eu.codlab.lorcana.math.MulliganCard
import eu.codlab.viewmodel.rememberViewModel

@Suppress("LongMethod")
@Composable
fun ShowMulliganCard(
    modifier: Modifier,
    model: EditMulliganModel,
    deck: DeckModel,
    holder: MulliganCard
) {
    var name by remember { mutableStateOf(TextFieldValue(holder.name)) }
    var amount by remember { mutableStateOf(TextFieldValue("${holder.amount}")) }

    val validationModel = rememberViewModel { ShowMulliganCardModel(deck, holder) }
    val validationState by validationModel.states.collectAsState()

    val maxWidth = rememberInputSize()

    val items: List<Quadruple<TextFieldValue, String, Boolean, (TextFieldValue) -> Unit>> = listOf(
        Quadruple(
            amount,
            Res.string.card_show_amount.localized(),
            validationState.amountValid
        ) {
            (it.asLongOrNull() ?: 0).let { newValue ->
                validationModel.update(newValue)

                model.updateScenario(
                    holder.id,
                    newValue
                )
            }
            amount = it
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
