package eu.codlab.lorcana.blipya.rph.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import eu.codlab.http.createClient
import eu.codlab.lorcana.blipya.rph.map.map.ShowEventInfo
import eu.codlab.lorcana.blipya.rph.models.EventHolder
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.lorcana.blipya.widgets.endOfDay
import eu.codlab.lorcana.blipya.widgets.startOfDay
import eu.codlab.maps.LatLng
import eu.codlab.maps.MapUtils
import eu.codlab.maps.XY
import eu.codlab.maps.providers.GoogleProvider
import eu.codlab.viewmodel.StateViewModel
import io.ktor.client.call.body
import io.ktor.client.request.get
import korlibs.time.DateTime
import korlibs.time.DateTimeTz
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import ovh.plrapps.mapcompose.api.addCallout
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.addMarker
import ovh.plrapps.mapcompose.api.centroidSnapshotFlow
import ovh.plrapps.mapcompose.api.enableRotation
import ovh.plrapps.mapcompose.api.maxScale
import ovh.plrapps.mapcompose.api.minScale
import ovh.plrapps.mapcompose.api.removeCallout
import ovh.plrapps.mapcompose.api.removeMarker
import ovh.plrapps.mapcompose.api.scale
import ovh.plrapps.mapcompose.api.setMagnifyingFactor
import ovh.plrapps.mapcompose.api.snapScrollTo
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.state.MapState

data class RphMapModelState(
    val currentCoordinates: LatLng? = null,
    val initialized: Boolean = false,
    val events: List<EventHolder> = emptyList(),
    val selectedDate: DateTimeTz = DateTime.now().local.endOfDay,
)

class RphMapModel(
    private val uriHandler: UriHandler
) : StateViewModel<RphMapModelState>(RphMapModelState()) {
    private val client = createClient { }
    private val googleApi = GoogleProvider()

    private val tileStreamProvider = TileStreamProvider { row, col, zoomLvl ->
        googleApi.getTile(row, col, zoomLvl)
    }

    private val levelCount = 18
    private val fullWidthOrHeight = 256 * 2.0.pow(levelCount).toInt()

    val mapState = MapState(
        levelCount,
        fullWidthOrHeight,
        fullWidthOrHeight,
        tileSize = 256
    ).apply {
        setMagnifyingFactor(1)

        maxScale = MAX_ZOOM
        scale = DEFAULT_ZOOM
        addLayer(tileStreamProvider)

        enableRotation()
    }


    init {
        val latLng = LatLng(0.0, 0.0)
        val xy = MapUtils.latLngToXY(latLng)

        safeLaunch(
            onError = {
                it.printStackTrace()
            }
        ) {
            googleApi.initialize()

            startLoadingData(states.value.selectedDate)

            updateState { copy(initialized = true) }

            setZoom(MIN_ZOOM)
            mapState.snapScrollTo(
                xy.x,
                xy.y
            )

            mapState.centroidSnapshotFlow().collect {
                val coords = MapUtils.xyToLatLng(it.x, it.y)
                updateState { copy(currentCoordinates = coords) }
            }
        }
    }

    fun moveToOrigin() {
        mapState.scale = DEFAULT_ZOOM

        safeLaunch {
            val latLng = LatLng(0.0, 0.0)
            val xy = MapUtils.latLngToXY(latLng)

            mapState.snapScrollTo(
                xy.x,
                xy.y
            )
        }
    }

    fun zoomIn() = applyZoom(1.1)

    fun zoomOut() = applyZoom(0.9)

    private fun applyZoom(coef: Double) {
        // make sure that we are both 3.0f > scale > 0.05f
        val newZoom = mapState.scale * coef
        setZoom(max(min(newZoom, MAX_ZOOM), MIN_ZOOM))
    }

    private fun setZoom(zoom: Double) {
        mapState.scale = zoom

        println("apply coef ${mapState.minScale} ${mapState.maxScale} $zoom")
        println("laoding coordinates ? ${states.value.currentCoordinates}")
        safeLaunch {
            states.value.currentCoordinates!!.let {
                println("having coordinates ? $it")
                val coords = MapUtils.latLngToXY(it.latitude, it.longitude)
                println("snapTo $coords")
                mapState.snapScrollTo(coords.x, coords.y)
            }
        }
    }

    fun setSelectedDate(date: DateTimeTz) {
        updateState {
            copy(selectedDate = date.endOfDay)
        }

        startLoadingData(date)
    }

    private fun startLoadingData(date: DateTimeTz) = safeLaunch {
        val url = "https://api-lorcana.com/rph/events?" +
                "startingAt=${date.startOfDay.utc.unixMillisLong}" +
                "&startingLast=${date.endOfDay.utc.unixMillisLong}"
        println("url -> $url")
        val result = try {
            client.get(url)
        } catch (err: Throwable) {
            err.printStackTrace()
            null
        }

        val events = result?.body<List<EventHolder>>()
            ?.filter { null != it.latLng() } ?: emptyList()

        println("having ${events.size} results")

        states.value.events.forEach { (event) -> mapState.removeMarker("event_${event.id}") }

        updateState {
            copy(
                events = events
            )
        }

        events.forEach { event ->
            val latLng = event.latLng()!!
            val xy = MapUtils.latLngToXY(latLng.latitude, latLng.longitude)

            mapState.addMarker("event_${event.event.id}", xy.x, xy.y, c = composable(event, xy))
        }
    }

    private fun flushCallouts() {
        mapState.onPress()
    }

    private fun composable(event: EventHolder, xy: XY): @Composable () -> Unit {
        val latLng = event.latLng()!!
        val id = "${latLng.latitude}${latLng.longitude}_callout"
        return {
            Column(
                Modifier.width(8.dp).height(8.dp)
                    .clickable {
                        flushCallouts()
                        mapState.addCallout(
                            id,
                            xy.x,
                            xy.y
                        ) {
                            ShowEventInfo(
                                Modifier.width(150.dp).height(150.dp).padding(bottom = 10.dp),
                                event
                            ) { (event) ->
                                // add to state ?
                                mapState.removeCallout(id)
                                uriHandler.openUri("https://tcg.ravensburgerplay.com/events/${event.id}")
                            }
                        }
                    }
                    .clip(
                        RoundedCornerShape(4.dp)
                    )
                    .background(AppColor.BackgroundDarkBlue)
            ) {
                // nothing
            }
        }
    }
}

private val MAX_ZOOM = 3.0
private val MIN_ZOOM = 0.00003
private val DEFAULT_ZOOM = 0.0003
