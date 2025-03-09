package eu.codlab.lorcana.blipya.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.bg_dark
import eu.codlab.blipya.res.bg_light
import eu.codlab.blipya.res.img_frame_top_left
import eu.codlab.blipya.res.img_frame_top_right
import eu.codlab.blipya.res.img_frame_under_left
import eu.codlab.blipya.res.img_frame_under_right
import eu.codlab.compose.theme.LocalDarkTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun BackgroundWrapper(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier) {
        BackgroundImage()

        content()

        DrawOverlay()
    }
}

@Composable
private fun BoxScope.BackgroundImage() {
    val painter = if (LocalDarkTheme.current) {
        painterResource(Res.drawable.bg_dark)
    } else {
        painterResource(Res.drawable.bg_light)
    }

    Image(
        modifier = Modifier.fillMaxSize().align(Alignment.Center),
        painter = painter,
        contentDescription = "",
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun BoxScope.DrawOverlay() {
    listOf(
        Res.drawable.img_frame_top_left to Alignment.TopStart,
        Res.drawable.img_frame_top_right to Alignment.TopEnd,
        Res.drawable.img_frame_under_left to Alignment.BottomStart,
        Res.drawable.img_frame_under_right to Alignment.BottomEnd,
    ).map { (res, align) ->
        Image(
            modifier = Modifier.width(140.dp).height(140.dp).align(align),
            painter = painterResource(res),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}