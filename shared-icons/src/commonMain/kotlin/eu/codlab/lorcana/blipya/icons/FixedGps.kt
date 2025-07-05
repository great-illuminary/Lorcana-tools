package eu.codlab.lorcana.blipya.icons

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Icons.Rounded.FixedGps: ImageVector
    get() {
        if (_fixedGps != null) {
            return _fixedGps!!
        }
        _fixedGps = Builder(name = "FixedGps", defaultWidth = 22.0.dp, defaultHeight = 22.0.dp,
                viewportWidth = 22.0f, viewportHeight = 22.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = EvenOdd) {
                moveTo(11.0f, 7.0f)
                curveTo(8.8f, 7.0f, 7.0f, 8.8f, 7.0f, 11.0f)
                curveTo(7.0f, 13.2f, 8.8f, 15.0f, 11.0f, 15.0f)
                curveTo(13.2f, 15.0f, 15.0f, 13.2f, 15.0f, 11.0f)
                curveTo(15.0f, 8.8f, 13.2f, 7.0f, 11.0f, 7.0f)
                lineTo(11.0f, 7.0f)
                close()
                moveTo(19.9f, 10.0f)
                curveTo(19.4f, 5.8f, 16.1f, 2.5f, 12.0f, 2.1f)
                lineTo(12.0f, 0.0f)
                lineTo(10.0f, 0.0f)
                lineTo(10.0f, 2.1f)
                curveTo(5.8f, 2.5f, 2.5f, 5.8f, 2.1f, 10.0f)
                lineTo(0.0f, 10.0f)
                lineTo(0.0f, 12.0f)
                lineTo(2.1f, 12.0f)
                curveTo(2.6f, 16.2f, 5.9f, 19.5f, 10.0f, 19.9f)
                lineTo(10.0f, 22.0f)
                lineTo(12.0f, 22.0f)
                lineTo(12.0f, 19.9f)
                curveTo(16.2f, 19.4f, 19.5f, 16.1f, 19.9f, 12.0f)
                lineTo(22.0f, 12.0f)
                lineTo(22.0f, 10.0f)
                lineTo(19.9f, 10.0f)
                lineTo(19.9f, 10.0f)
                close()
                moveTo(11.0f, 18.0f)
                curveTo(7.1f, 18.0f, 4.0f, 14.9f, 4.0f, 11.0f)
                curveTo(4.0f, 7.1f, 7.1f, 4.0f, 11.0f, 4.0f)
                curveTo(14.9f, 4.0f, 18.0f, 7.1f, 18.0f, 11.0f)
                curveTo(18.0f, 14.9f, 14.9f, 18.0f, 11.0f, 18.0f)
                lineTo(11.0f, 18.0f)
                close()
            }
        }
        .build()
        return _fixedGps!!
    }

private var _fixedGps: ImageVector? = null
