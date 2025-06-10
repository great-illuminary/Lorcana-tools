package eu.codlab.lorcana.blipya.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.compose.widgets.preview.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.theme.AppColor
import io.github.koalaplot.core.line.AreaBaseline
import io.github.koalaplot.core.line.AreaPlot
import io.github.koalaplot.core.style.AreaStyle
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.xygraph.DefaultPoint
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.IntLinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.XYGraphScope
import io.github.koalaplot.core.xygraph.rememberAxisStyle
import kotlin.math.ceil
import kotlin.math.max

@Suppress("LongMethod", "MagicNumber")
@Composable
fun ShowLineChartCard(
    modifier: Modifier = Modifier,
    title: String,
    xAxisTitles: List<String>,
    values: List<Triple<String, List<Float>, Color>>,
    showLegend: Boolean = true,
    panZoomEnabled: Boolean = false,
    yAxisLabels: (@Composable (String) -> Unit)? = null,
    xAxisLabels: (@Composable (String) -> Unit)? = null
) {
    if (xAxisTitles.isEmpty()) {
        throw IllegalStateException("xAxisTitle are empty with $title !")
    }

    Column(modifier = modifier.padding(4.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp,
            backgroundColor = defaultCardBackground()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextNormal(
                    text = title,
                    textAlign = TextAlign.Center
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    if (showLegend) {
                        Column {
                            values.map {
                                Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                    Spacer(
                                        modifier = Modifier
                                            .height(16.dp)
                                            .width(16.dp)
                                            .background(it.third)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    TextNormal(text = it.first, fontSize = 10.sp)
                                }
                            }
                        }
                    }

                    val max = max(
                        1f,
                        values.map { triple -> triple.second.maxOrNull() ?: 0f }.maxOrNull() ?: 0f
                    )

                    println("max is $max")

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (xAxisTitles.isNotEmpty()) {
                            XYGraph(
                                panEnabled = panZoomEnabled,
                                xAxisModel = IntLinearAxisModel(
                                    xAxisTitles.indices
                                ),
                                yAxisModel = FloatLinearAxisModel(
                                    0f..(ceil(max / 50.0) * 50.0).toFloat(),
                                    minimumMajorTickSpacing = 50.dp
                                ),
                                xAxisLabels = {
                                    if (null != xAxisLabels) {
                                        xAxisLabels(xAxisTitles[it])
                                    } else {
                                        TextNormal(
                                            text = xAxisTitles[it],
                                            fontSize = 10.sp
                                        )
                                        // AxisLabel(it, Modifier.padding(top = 2.dp))
                                    }
                                },
                                xAxisTitle = {
                                    //
                                },
                                yAxisLabels = {
                                    if (null != yAxisLabels) {
                                        yAxisLabels("$it")
                                    } else {
                                        TextNormal(
                                            text = "$it",
                                            fontSize = 10.sp
                                        )
                                        // AxisLabel(it, Modifier.padding(top = 2.dp))
                                    }
                                },
                                yAxisTitle = {
                                    //
                                },
                                xAxisStyle = rememberAxisStyle(
                                    color = Color.Transparent
                                ),
                                yAxisStyle = rememberAxisStyle(
                                    color = Color.Transparent
                                ),
                                horizontalMajorGridLineStyle = null,
                                horizontalMinorGridLineStyle = null,
                                verticalMajorGridLineStyle = null,
                                verticalMinorGridLineStyle = null
                            ) {
                                values.map { triple ->
                                    val list = triple.second

                                    Chart(
                                        list.mapIndexed { index, d -> DefaultPoint(index, d) },
                                        triple.third
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun XYGraphScope<Int, Float>.Chart(
    data: List<DefaultPoint<Int, Float>>,
    color: Color
) {
    AreaPlot(
        data = data,
        lineStyle = LineStyle(
            brush = SolidColor(color),
            strokeWidth = 2.dp
        ),
        areaBaseline = AreaBaseline.ConstantLine<Int, Float>(0f),
        areaStyle = AreaStyle(
            brush = SolidColor(color),
            alpha = 0.3f
        )
    )
}

@Suppress("MagicNumber")
@Composable
@HotPreview(widthDp = 300, heightDp = 150, darkMode = true)
@HotPreview(widthDp = 300, heightDp = 150, darkMode = false)
private fun ShowLineChartCardPreview() {
    PreviewDarkLightColumn { _, _ ->
        val values = listOf(6, 5, 4, 3, 2, 1, 0)
        ShowLineChartCard(
            title = "Test",
            xAxisTitles = values.map { it.toString() },
            yAxisLabels = null,
            xAxisLabels = null,
            values = listOf(
                Triple(
                    "iOS",
                    values.map { it * 2 }.map { it.toFloat() },
                    AppColor.Red
                ),
                Triple(
                    "Android",
                    values.map { it.toFloat() },
                    AppColor.Blue
                )
            )
        )
    }
}
