package eu.codlab.lorcana.blipya.rph.map.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.rph.map.map.ShowEventInfo
import eu.codlab.lorcana.blipya.rph.map.map.ShowStoreInfo
import eu.codlab.lorcana.blipya.rph.models.EventHolder
import eu.codlab.lorcana.blipya.rph.models.Store
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.maps.XY
import ovh.plrapps.mapcompose.api.addCallout
import ovh.plrapps.mapcompose.api.removeCallout
import ovh.plrapps.mapcompose.ui.state.MapState

sealed class Composer<T>(
    protected val uriHandler: UriHandler,
    protected val mapState: MapState,
    protected val flushCallouts: () -> Unit
) {
    @Composable
    fun cluster(size: Int) {
        Box(
            modifier = Modifier
                .background(AppColor.BackgroundDarkBlue, shape = CircleShape)
                .size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            TextNormal(text = size.toString(), color = AppColor.WhiteCream)
        }
    }

    abstract fun compose(model: T, xy: XY): @Composable () -> Unit
}

class ComposeStoreHolder(
    uriHandler: UriHandler,
    mapState: MapState,
    flushCallouts: () -> Unit
) : Composer<Store>(uriHandler, mapState, flushCallouts) {
    override fun compose(model: Store, xy: XY): @Composable (() -> Unit) {
        val latLng = model.latLng()!!
        val id = "${latLng.latitude}${latLng.longitude}_callout"
        return {
            Column(
                Modifier.width(16.dp).height(16.dp)
                    .clickable {
                        flushCallouts()
                        mapState.addCallout(
                            id,
                            xy.x,
                            xy.y
                        ) {
                            ShowStoreInfo(
                                Modifier.width(150.dp).height(150.dp).padding(bottom = 10.dp),
                                model
                            ) { store ->
                                // add to state ?
                                mapState.removeCallout(id)
                                uriHandler.openUri("https://tcg.ravensburgerplay.com/stores/${store.uuid}")
                            }
                        }
                    }
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .background(AppColor.BackgroundDarkBlue)
            ) {
                // nothing
            }
        }
    }
}

class ComposerEventHolder(
    uriHandler: UriHandler,
    mapState: MapState,
    flushCallouts: () -> Unit
) : Composer<EventHolder>(uriHandler, mapState, flushCallouts) {
    override fun compose(model: EventHolder, xy: XY): @Composable () -> Unit {
        val latLng = model.latLng()!!
        val id = "${latLng.latitude}${latLng.longitude}_callout"
        return {
            Column(
                Modifier.width(16.dp).height(16.dp)
                    .clickable {
                        flushCallouts()
                        mapState.addCallout(
                            id,
                            xy.x,
                            xy.y
                        ) {
                            ShowEventInfo(
                                Modifier.width(150.dp).height(150.dp).padding(bottom = 10.dp),
                                model
                            ) { (event) ->
                                // add to state ?
                                mapState.removeCallout(id)
                                uriHandler.openUri("https://tcg.ravensburgerplay.com/events/${event.id}")
                            }
                        }
                    }
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .background(AppColor.BackgroundDarkBlue)
            ) {
                // nothing
            }
        }
    }
}
