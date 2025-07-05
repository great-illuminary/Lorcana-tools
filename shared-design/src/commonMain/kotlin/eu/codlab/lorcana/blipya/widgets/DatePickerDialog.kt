package eu.codlab.lorcana.blipya.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.config.rememberBasisEpicCalendarConfig
import epicarchitect.calendar.compose.datepicker.EpicDatePicker
import epicarchitect.calendar.compose.datepicker.config.rememberEpicDatePickerConfig
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState
import epicarchitect.calendar.compose.datepicker.state.rememberEpicDatePickerState
import epicarchitect.calendar.compose.pager.config.rememberEpicCalendarPagerConfig
import eu.codlab.blipya.design.res.Res
import eu.codlab.blipya.design.res.april
import eu.codlab.blipya.design.res.august
import eu.codlab.blipya.design.res.december
import eu.codlab.blipya.design.res.february
import eu.codlab.blipya.design.res.january
import eu.codlab.blipya.design.res.july
import eu.codlab.blipya.design.res.june
import eu.codlab.blipya.design.res.march
import eu.codlab.blipya.design.res.may
import eu.codlab.blipya.design.res.november
import eu.codlab.blipya.design.res.october
import eu.codlab.blipya.design.res.september
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.utils.localized
import korlibs.io.async.launch
import korlibs.time.DateFormat
import korlibs.time.DateTime
import korlibs.time.DateTimeTz
import korlibs.time.days
import korlibs.time.hours
import korlibs.time.milliseconds
import korlibs.time.minutes
import korlibs.time.months
import korlibs.time.seconds
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.StringResource
import korlibs.time.Month as KorlibsMonth

@Composable
fun DatePickerDialog(
    defaultDate: DateTimeTz? = null,
    onDismiss: () -> Unit,
    onDate: (DateTimeTz) -> Unit
) {
    val initialMonth =
        defaultDate?.let { EpicMonth(it.yearInt, it.month.toDateTimeMonth) } ?: EpicMonth.now()
    val coroutine = rememberCoroutineScope()
    val state = rememberEpicDatePickerState(
        selectionMode = EpicDatePickerState.SelectionMode.Single(),
        initialMonth = initialMonth,
        config = rememberEpicDatePickerConfig(
            pagerConfig = rememberEpicCalendarPagerConfig(
                basisConfig = rememberBasisEpicCalendarConfig(
                    displayDaysOfAdjacentMonths = false
                )
            ),
            selectionContentColor = MaterialTheme.colorScheme.onPrimary,
            selectionContainerColor = MaterialTheme.colorScheme.primary
        )
    )

    LaunchedEffect(state.selectedDates) {
        if (state.selectedDates.isNotEmpty()) {
            state.selectedDates.firstOrNull()?.let {
                val date = DateTime.invoke(
                    year = it.year,
                    month = it.monthNumber,
                    day = it.dayOfMonth,
                    hour = 12
                )

                println(date.local)
                val endOfDay = date.local.endOfDay
                println(endOfDay)
                println("selected date utc is ${endOfDay.utc.format(DateFormat.FORMAT2)}")
                println("selected date is ${endOfDay.format(DateFormat.FORMAT1)}")

                onDate(date.local.endOfDay)
                /*
                val zone = kotlinx.datetime.TimeZone.currentSystemDefault()
                val timestamp = it.atStartOfDayIn(zone).plus(12.hours)

                println("selected date was $it -> ${timestamp.toEpochMilliseconds()}")
                val dateTime = DateTime.fromUnixMillis(timestamp.toEpochMilliseconds())

                val localDateTime = timestamp.toLocalDateTime(zone)
                println("comparing both -> $localDateTime / ${dateTime.local}")

                onDate(dateTime)
                 */
            }
        }
    }

    val currentMonth = state.pagerState.currentMonth
    val month = currentMonth.month.translation.localized()
    val year = currentMonth.year

    val isDark = LocalDarkTheme.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        DefaultCard(
            backgroundColor = AppColor.White /*?: if (isDark) {
                Color.Black
            } else {
                Color.White
            }*/
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = {
                            coroutine.launch {
                                state.pagerState.scrollMonths(-1)
                            }
                        }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Default.SkipPrevious),
                            tint = AppColor.Black,
                            contentDescription = "previous"
                        )
                    }
                    Column(Modifier.weight(1.0f)) { }
                    TextNormal(
                        color = Color.Black,
                        text = "$month $year"
                    )
                    Column(Modifier.weight(1.0f)) { }

                    IconButton(
                        onClick = {
                            coroutine.launch {
                                state.pagerState.scrollMonths(1)
                            }
                        }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Default.SkipNext),
                            tint = AppColor.Black,
                            contentDescription = "next"
                        )
                    }
                }

                EpicDatePicker(state = state)
            }
        }
    }
}

val Month.translation: StringResource
    get() = when (this) {
        Month.JANUARY -> Res.string.january
        Month.FEBRUARY -> Res.string.february
        Month.MARCH -> Res.string.march
        Month.APRIL -> Res.string.april
        Month.MAY -> Res.string.may
        Month.JUNE -> Res.string.june
        Month.JULY -> Res.string.july
        Month.AUGUST -> Res.string.august
        Month.SEPTEMBER -> Res.string.september
        Month.OCTOBER -> Res.string.october
        Month.NOVEMBER -> Res.string.november
        Month.DECEMBER -> Res.string.december
        else -> Res.string.january
    }

val KorlibsMonth.toDateTimeMonth: Month
    get() = when (this) {
        KorlibsMonth.January -> Month.JANUARY
        KorlibsMonth.February -> Month.FEBRUARY
        KorlibsMonth.March -> Month.MARCH
        KorlibsMonth.April -> Month.APRIL
        KorlibsMonth.May -> Month.MAY
        KorlibsMonth.June -> Month.JUNE
        KorlibsMonth.July -> Month.JULY
        KorlibsMonth.August -> Month.AUGUST
        KorlibsMonth.September -> Month.SEPTEMBER
        KorlibsMonth.October -> Month.OCTOBER
        KorlibsMonth.November -> Month.NOVEMBER
        KorlibsMonth.December -> Month.DECEMBER
    }

val DateTimeTz.startOfDay: DateTimeTz
    get() = this.minus(this.hours.hours)
        .minus(this.minutes.minutes)
        .minus(this.seconds.seconds)
        .minus(this.milliseconds.milliseconds)

val DateTimeTz.endOfDay: DateTimeTz
    get() = this.add(0.months, 1.days).startOfDay.minus(1.milliseconds)

