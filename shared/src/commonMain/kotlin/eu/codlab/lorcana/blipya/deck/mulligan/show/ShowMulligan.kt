package eu.codlab.lorcana.blipya.deck.mulligan.show

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.LocalNavigator
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.delete
import eu.codlab.blipya.res.edit
import eu.codlab.blipya.res.show_scenario_delete_text
import eu.codlab.blipya.res.show_scenario_delete_title
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.DeckConfigurationModel
import eu.codlab.lorcana.blipya.deck.edit.DisplayStatisticalResult
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.navigate.NavigateTo
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.MenuItemOverflowMenu
import eu.codlab.lorcana.blipya.widgets.PopupConfirm
import eu.codlab.lorcana.math.MulliganScenario
import eu.codlab.viewmodel.rememberViewModel

@Composable
fun ShowMulligan(
    modifier: Modifier,
    app: AppModel,
    parentModel: DeckConfigurationModel,
    deck: DeckModel,
    mulligan: MulliganScenario
) {
    val model = rememberViewModel { ShowMulliganModel(deck, mulligan) }
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
            title = Res.string.show_scenario_delete_title.localized(),
            text = Res.string.show_scenario_delete_text.localized(),
            onDismiss = { promptDelete = false },
            onConfirm = {
                promptDelete = false
                parentModel.delete(mulligan)
                navigator?.pop()
            }
        )
    }

    Column(modifier) {
        Row {
            TextNormal(
                modifier = Modifier.weight(1.0f),
                text = state.name
            )

            MenuItemOverflowMenu(
                tint = Color.White,
                imageVector = Icons.Outlined.MoreVert,
                id = mulligan.id,
                contentDescription = "Manage"
            ) {
                // show the actions to edit & delete the current scenario
                listOf(
                    Triple(
                        Res.string.edit.localized(),
                        Icons.Default.Edit
                    ) { app.show(NavigateTo.DeckMulligan(state.deck, state.mulligan)) },
                    Triple(
                        "${Res.string.delete.localized()} ${state.name}",
                        Icons.Default.Delete
                    ) { promptDelete = true },
                ).forEach { (contentDescription, imageVector, onClick) ->
                    IconButton(onClick) {
                        Icon(
                            imageVector,
                            contentDescription,
                            tint = tintColor
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(AppSizes.paddings.reduced))

        DisplayStatisticalResult(probability = state.probability)
    }
}
