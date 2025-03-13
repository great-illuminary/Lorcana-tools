package eu.codlab.lorcana.blipya.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun BottomSpacer(modifier: Modifier = Modifier) {
    val height = WindowInsets.navigationBars.getBottom(LocalDensity.current)
    val scaled = (height / LocalDensity.current.density).roundToInt()

    Column(
        modifier = modifier.height(scaled.dp).background(Color.Red)
    ) {
        Spacer(Modifier.height(scaled.dp))
    }
}
