package eu.codlab.lorcana.blipya.rph.own

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import eu.codlab.compose.widgets.CustomOutlinedEditText
import eu.codlab.lorcana.blipya.rph.models.User

@Composable
fun ShowSelectUser(
    modifier: Modifier,
    model: RphOwnRegistrationsModel
) {
    val state by model.states.collectAsState()

    var options by remember { mutableStateOf(emptyList<User>()) }

    val matching = state.matchingUsers
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(matching) {
        val newOptions: List<User> = when (matching) {
            is DataLoader.Error<List<User>> -> emptyList()
            is DataLoader.Loaded<List<User>> -> {
                matching.data.also {
                    println(it)
                }
            }

            is DataLoader.Loading<List<User>> -> emptyList()
            null -> emptyList()
        }

        if (options != newOptions) {
            options = newOptions
        }

        if (options.isNotEmpty()) {
            expanded = true
        }
    }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .clickable { expanded = !expanded },
    ) {
        CustomOutlinedEditText(
            value = text,
            onValueChanged = {
                println("new value $it")
                text = it
                model.startMatchingUser(it.text)
            },
            modifier = Modifier.padding(start = 10.dp),
        )

        DropdownMenu(
            expanded = expanded,
            properties = PopupProperties(
                focusable = false
            ),
            onDismissRequest = { expanded = false }
        ) {
            if (options.size > 10) {
                expanded = false
                return@DropdownMenu
            }

            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    onClick = {
                        model.selectSpecificUser(option)
                        expanded = false
                    }
                ) {
                    Text(
                        text = listOfNotNull(
                            option.id,
                            option.bestIdentifier,
                            option.bestIdentifierInGame
                        ).joinToString(" / ")
                    )
                }
            }
        }
    }
}