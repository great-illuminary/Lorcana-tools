package eu.codlab.lorcana.blipya.deck.scenario

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.DeckConfigurationModel
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.widgets.PopupConfirm
import eu.codlab.lorcana.math.Scenario
import eu.codlab.viewmodel.rememberViewModel

@Composable
fun ShowScenario(
    modifier: Modifier,
    app: AppModel,
    parentModel: DeckConfigurationModel,
    deck: DeckModel,
    scenario: Scenario
) {
    val model = rememberViewModel { ShowScenarioModel(app, deck, scenario) }
    var promptDelete by remember { mutableStateOf(false) }
    val state by model.states.collectAsState()

    val tintColor = if (LocalDarkTheme.current) {
        AppColor.White
    } else {
        AppColor.Black
    }
    val navigator = LocalNavigator.current

    if (promptDelete) {
        PopupConfirm(
            show = true,
            title = "Delete the Scenario",
            text = "Are you sure to delete this scenario ?",
            onDismiss = { promptDelete = false },
            onConfirm = {
                promptDelete = false
                parentModel.delete(scenario)
                navigator?.pop()
            }
        )
    }

    LaunchedEffect(Unit) {
        model.triggerProbability()
    }

    Column {
        TextNormal(state.name)

        Spacer(Modifier.height(8.dp))

        Row {
            if (state.probability < 0) {
                TextNormal("Result : invalid")
            } else {
                TextNormal("Result : ${state.probability.round(2)}%")
            }
        }

        Row {
            IconButton(
                onClick = {
                    app.show(state.deck, state.scenario)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit ${state.name}",
                    tint = tintColor
                )
            }

            IconButton(
                onClick = {
                    promptDelete = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete ${state.name}",
                    tint = tintColor
                )
            }
        }
    }
}

@Composable
fun rememberInputSize(min: Dp = 90.dp) = when (LocalWindow.current) {
    WindowType.SMARTPHONE_TINY -> min
    WindowType.SMARTPHONE -> min
    WindowType.PHABLET -> 90.dp
    WindowType.TABLET -> 100.dp
}
