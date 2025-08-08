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

public val Icons.Rounded.PieChart: ImageVector
    get() {
        if (_piechart != null) {
            return _piechart!!
        }
        _piechart = Builder(name = "Rounded.PieChart",
                defaultWidth = 16.0.dp, defaultHeight = 16.0.dp, viewportWidth = 16.0f,
                viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.0f, 16.0f)
                curveToRelative(4.079f, 0.0f, 7.438f, -3.055f, 7.931f, -7.0f)
                horizontalLineTo(7.778f)
                lineToRelative(-5.027f, 5.027f)
                curveTo(4.156f, 15.253f, 5.989f, 16.0f, 8.0f, 16.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.0f, 0.0f)
                verticalLineToRelative(8.0f)
                horizontalLineToRelative(8.0f)
                curveTo(16.0f, 3.582f, 12.418f, 0.0f, 8.0f, 0.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(0.0f, 8.0f)
                curveToRelative(0.0f, 2.047f, 0.775f, 3.909f, 2.04f, 5.324f)
                lineTo(7.0f, 8.364f)
                verticalLineTo(8.0f)
                verticalLineTo(0.069f)
                curveTo(3.055f, 0.562f, 0.0f, 3.921f, 0.0f, 8.0f)
                close()
            }
        }
        .build()
        return _piechart!!
    }

private var _piechart: ImageVector? = null
