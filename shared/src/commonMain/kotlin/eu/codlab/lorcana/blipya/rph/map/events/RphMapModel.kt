package eu.codlab.lorcana.blipya.rph.map.events

import androidx.compose.ui.platform.UriHandler
import eu.codlab.lorcana.blipya.rph.map.map.interfaces.MapInterfaceCalendar
import eu.codlab.lorcana.blipya.rph.map.map.interfaces.MapInterfaceCalendarState
import eu.codlab.lorcana.blipya.rph.models.EventHolder
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.lorcana.blipya.widgets.endOfDay
import eu.codlab.lorcana.blipya.widgets.startOfDay
import eu.codlab.maps.LatLng
import eu.codlab.maps.MapUtils
import io.ktor.client.call.body
import io.ktor.client.request.get
import korlibs.time.DateTime
import korlibs.time.DateTimeTz
import ovh.plrapps.mapcompose.api.addMarker
import ovh.plrapps.mapcompose.api.removeMarker

data class RphMapModelState(
    override val currentCoordinates: LatLng? = null,
    override val initialized: Boolean = false,
    val events: List<EventHolder> = emptyList(),
    override val selectedDate: DateTimeTz = DateTime.now().local.endOfDay,
) : MapInterfaceCalendarState, MapModelState

class RphMapModel(
    private val uriHandler: UriHandler
) : MapModel<RphMapModelState, EventHolder>(
    initialState = RphMapModelState(),
    allocateComposer = { mapState, flushCallouts -> ComposerEventHolder(uriHandler, mapState, flushCallouts) }
), MapInterfaceCalendar {

    override fun setSelectedDate(dateTimeTz: DateTimeTz) {
        updateState {
            copy(selectedDate = dateTimeTz.endOfDay)
        }

        startLoadingData(dateTimeTz)
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

            mapState.addMarker("event_${event.event.id}", xy.x, xy.y, c = composer.compose(event, xy))
        }
    }

    override suspend fun startLoadingData(state: RphMapModelState) {
        startLoadingData(state.selectedDate)
    }

    override suspend fun setInitialized(initialized: Boolean) =
        updateState { copy(initialized = true) }

    override suspend fun setCurrentCoordinates(newCoordinates: LatLng) =
        updateState { copy(currentCoordinates = newCoordinates) }
}
