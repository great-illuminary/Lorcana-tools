package eu.codlab.lorcana.blipya.init

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import eu.codlab.compose.widgets.StatusBarAndNavigation
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightRow

@Composable
fun InitializeScreen(
    globalApp: AppModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        if (!globalApp.isInitialized()) {
            globalApp.initialize()
        }
    }

    StatusBarAndNavigation()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.BackgroundBlue)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(
                    RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp
                    )
                )
                .heightIn(0.dp, 400.dp) // mention max height here
                .widthIn(0.dp, 800.dp) // mention max width here
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
fun InitializeScreenPreview() {
    PreviewDarkLightRow(
        modifier = Modifier.fillMaxSize(),
        submodifier = { Modifier.weight(1f).fillMaxHeight() }
    ) { _, _ ->
        InitializeScreen(
            globalApp = AppModel("", ""),
            modifier = Modifier
                .width(600.dp)
                .height(300.dp)
        )
    }
}
