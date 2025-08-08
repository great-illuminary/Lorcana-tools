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

public val Icons.Rounded.ZoomIn: ImageVector
    get() {
        if (_zoomIn != null) {
            return _zoomIn!!
        }
        _zoomIn = Builder(name = "ZoomIn", defaultWidth = 22.0.dp, defaultHeight = 22.0.dp,
                viewportWidth = 96.0f, viewportHeight = 96.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(54.0f, 0.0f)
                arcTo(42.051f, 42.051f, 0.0f, false, false, 12.0f, 42.0f)
                arcToRelative(41.599f, 41.599f, 0.0f, false, false, 8.48f, 25.036f)
                lineTo(1.758f, 85.758f)
                arcToRelative(5.999f, 5.999f, 0.0f, true, false, 8.484f, 8.484f)
                lineTo(28.964f, 75.52f)
                arcTo(41.599f, 41.599f, 0.0f, false, false, 54.0f, 84.0f)
                arcTo(42.0f, 42.0f, 0.0f, false, false, 54.0f, 0.0f)
                close()
                moveTo(54.0f, 72.0f)
                arcTo(30.0f, 30.0f, 0.0f, true, true, 84.0f, 42.0f)
                arcTo(30.031f, 30.031f, 0.0f, false, true, 54.0f, 72.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(66.0f, 36.0f)
                horizontalLineTo(60.0f)
                verticalLineTo(30.0f)
                arcToRelative(6.0f, 6.0f, 0.0f, false, false, -12.0f, 0.0f)
                verticalLineToRelative(6.0f)
                horizontalLineTo(42.0f)
                arcToRelative(6.0f, 6.0f, 0.0f, false, false, 0.0f, 12.0f)
                horizontalLineToRelative(6.0f)
                verticalLineToRelative(6.0f)
                arcToRelative(6.0f, 6.0f, 0.0f, false, false, 12.0f, 0.0f)
                verticalLineTo(48.0f)
                horizontalLineToRelative(6.0f)
                arcToRelative(6.0f, 6.0f, 0.0f, false, false, 0.0f, -12.0f)
                close()
            }
        }
        .build()
        return _zoomIn!!
    }

private var _zoomIn: ImageVector? = null
