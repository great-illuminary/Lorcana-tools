package eu.codlab.lorcana.blipya.deck.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.invalid
import eu.codlab.blipya.res.otd
import eu.codlab.blipya.res.otp
import eu.codlab.blipya.res.result
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.scenario.show.round
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.math.tools.MulliganResult

@Composable
fun DisplayStatisticalResult(
    modifier: Modifier = Modifier,
    probability: Double
) {
    Row(modifier) {
        TextNormal("${Res.string.result.localized()} : ${probability.showProbability()}")
    }
}

@Composable
fun DisplayStatisticalResult(
    modifier: Modifier = Modifier,
    probability: MulliganResult
) {
    Column(modifier) {
        Row(modifier) {
            TextNormal("${Res.string.otp.localized()} : ${probability.onPlay.showProbability()}")
        }

        Row(modifier) {
            TextNormal("${Res.string.otd.localized()} : ${probability.onDraw.showProbability()}")
        }
    }
}

@Composable
fun Double.showProbability() = if (this < 0) {
    Res.string.invalid.localized()
} else {
    "${round(2)}%"
}

@Suppress("UnusedPrivateMember", "MagicNumber")
@HotPreview(widthDp = 200, heightDp = 200)
@Composable
private fun DisplayStatisticalResultRegular() {
    HotPreviewApp(Modifier.fillMaxSize()) {
        DisplayStatisticalResult(Modifier.fillMaxWidth(), 80.0)
    }
}

@Suppress("UnusedPrivateMember", "MagicNumber")
@HotPreview(widthDp = 200, heightDp = 200)
@Composable
private fun DisplayStatisticalResultMulligan() {
    HotPreviewApp(Modifier.fillMaxSize()) {
        DisplayStatisticalResult(
            Modifier.fillMaxWidth(),
            MulliganResult(
                onPlay = 25.0,
                onDraw = 30.0
            )
        )
    }
}
