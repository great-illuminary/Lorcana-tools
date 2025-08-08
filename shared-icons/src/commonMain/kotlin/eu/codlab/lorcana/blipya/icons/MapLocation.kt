@file:Suppress("ktlint")
package eu.codlab.lorcana.blipya.icons

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Icons.Rounded.MapLocation: ImageVector
    get() {
        if (_mapLocation != null) {
            return _mapLocation!!
        }
        _mapLocation = Builder(name = "MapLocation", defaultWidth = 22.0.dp, defaultHeight =
                22.0.dp, viewportWidth = 576.0f, viewportHeight = 512.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(288.0f, 0.0f)
                curveTo(221.7f, 0.0f, 168.0f, 53.73f, 168.0f, 120.0f)
                curveToRelative(0.0f, 48.38f, 16.86f, 61.9f, 107.7f, 193.5f)
                curveToRelative(5.96f, 8.6f, 18.69f, 8.6f, 24.65f, 0.0f)
                curveTo(391.1f, 181.9f, 408.0f, 168.4f, 408.0f, 120.0f)
                curveTo(408.0f, 53.73f, 354.3f, 0.0f, 288.0f, 0.0f)
                close()
                moveTo(288.0f, 176.0f)
                curveTo(261.5f, 176.0f, 240.0f, 154.5f, 240.0f, 128.0f)
                reflectiveCurveTo(261.5f, 80.0f, 288.0f, 80.0f)
                curveToRelative(26.48f, 0.0f, 48.0f, 21.53f, 48.0f, 48.0f)
                reflectiveCurveTo(314.5f, 176.0f, 288.0f, 176.0f)
                close()
                moveTo(10.06f, 227.6f)
                curveTo(3.98f, 230.0f, 0.0f, 235.9f, 0.0f, 242.4f)
                verticalLineToRelative(253.5f)
                curveToRelative(0.0f, 11.32f, 11.49f, 19.04f, 22.0f, 14.84f)
                lineTo(160.0f, 448.0f)
                verticalLineTo(201.4f)
                curveTo(152.5f, 188.8f, 147.2f, 178.0f, 143.4f, 167.5f)
                lineTo(10.06f, 227.6f)
                close()
                moveTo(326.6f, 331.8f)
                curveTo(317.9f, 344.4f, 303.4f, 352.0f, 288.0f, 352.0f)
                curveToRelative(-15.42f, 0.0f, -29.86f, -7.57f, -38.66f, -20.28f)
                curveTo(233.2f, 308.3f, 196.9f, 256.6f, 192.0f, 249.6f)
                verticalLineTo(447.1f)
                lineTo(384.0f, 512.0f)
                verticalLineTo(249.6f)
                curveTo(379.1f, 256.6f, 342.8f, 308.3f, 326.6f, 331.8f)
                close()
                moveTo(554.1f, 161.2f)
                lineTo(416.0f, 224.0f)
                verticalLineToRelative(288.0f)
                lineToRelative(149.9f, -67.59f)
                curveTo(572.0f, 441.1f, 576.0f, 436.1f, 576.0f, 429.6f)
                verticalLineTo(176.0f)
                curveTo(576.0f, 164.7f, 564.6f, 156.1f, 554.1f, 161.2f)
                close()
            }
        }
        .build()
        return _mapLocation!!
    }

private var _mapLocation: ImageVector? = null
