package eu.codlab.lorcana.blipya.rph.map.map.interfaces

import korlibs.time.DateTimeTz
import kotlinx.coroutines.flow.StateFlow

sealed interface MapInterfaceAction {
}

interface MapInterfaceZoomable : MapInterfaceAction {
    fun zoomIn()

    fun zoomOut()

    fun moveToOrigin()
}

interface MapInterfaceCalendarState {
    val selectedDate: DateTimeTz
}

interface MapInterfaceCalendar : MapInterfaceAction {
    val states: StateFlow<MapInterfaceCalendarState>
    fun setSelectedDate(dateTimeTz: DateTimeTz)
}