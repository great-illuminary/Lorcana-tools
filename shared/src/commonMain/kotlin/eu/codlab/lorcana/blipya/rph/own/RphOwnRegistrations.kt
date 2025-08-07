package eu.codlab.lorcana.blipya.rph.own

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.lorcana.blipya.deck.expectedNumberOfColumns
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.rph.map.map.ShowEventInfo
import eu.codlab.lorcana.blipya.rph.models.EventHolder
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.viewmodel.rememberViewModel

@Composable
fun RphOwnRegistrations(
    modifier: Modifier,
    appModel: AppModel,
) {
    val uriHandler = LocalUriHandler.current
    val model = rememberViewModel { RphOwnRegistrationsModel(appModel) }
    val state by model.states.collectAsState()
    val events = state.events

    val numberOfColumnsForMainItem = 2
    val columns = expectedNumberOfColumns()

    LazyVerticalGrid(
        modifier = modifier.imePadding(),
        contentPadding = PaddingValues(AppSizes.paddings.default),
        columns = GridCells.Fixed(columns),
        verticalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
        horizontalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
    ) {
        item(span = { GridItemSpan(numberOfColumnsForMainItem) }) {
            ShowSelectUser(
                Modifier.fillMaxSize(),
                model
            )
        }

        item(span = { GridItemSpan(columns) }) {
            // nothing
        }

        if (null == events) {
            return@LazyVerticalGrid
        }

        when (events) {
            is DataLoader.Loading -> {
                // nothing
            }

            is DataLoader.Error<List<EventHolder>> -> {
            }

            is DataLoader.Loaded<List<EventHolder>> -> {
                items(
                    events.data.size
                ) { it ->
                    val event = events.data[it]

                    // ShowCard for the event
                    ShowEventInfo(
                        Modifier.fillMaxSize(),
                        event
                    ) { (event) ->
                        uriHandler.openUri("https://tcg.ravensburgerplay.com/events/${event.id}")
                    }
                }
            }
        }
    }
}

@HotPreview(widthDp = 500, heightDp = 400, darkMode = true)
@HotPreview(widthDp = 500, heightDp = 400, darkMode = false)
@Composable
private fun RphOwnRegistrationsPreview() {
    PreviewDarkLightColumn { modifier, _ ->
        RphOwnRegistrations(
            modifier,
            AppModel.fake()
        )
    }
}
