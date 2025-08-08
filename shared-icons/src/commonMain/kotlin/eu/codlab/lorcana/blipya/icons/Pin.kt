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

public val Icons.Rounded.Pin: ImageVector
    get() {
        if (_pin != null) {
            return _pin!!
        }
        _pin = Builder(name = "Pin", defaultWidth = 22.0.dp, defaultHeight = 30.0.dp, viewportWidth
                = 22.0f, viewportHeight = 30.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(11.0f, 0.0f)
                arcTo(11.012f, 11.012f, 0.0f, false, false, 0.0f, 11.0f)
                curveTo(0.0f, 21.361f, 9.952f, 29.442f, 10.375f, 29.781f)
                arcToRelative(1.001f, 1.001f, 0.0f, false, false, 1.249f, 0.0f)
                curveTo(12.048f, 29.442f, 22.0f, 21.361f, 22.0f, 11.0f)
                arcTo(11.012f, 11.012f, 0.0f, false, false, 11.0f, 0.0f)
                close()
            }
        }
        .build()
        return _pin!!
    }

private var _pin: ImageVector? = null
