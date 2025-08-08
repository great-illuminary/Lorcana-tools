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

public val Icons.Filled.SkipPrevious: ImageVector
    get() {
        if (_skipPrevious != null) {
            return _skipPrevious!!
        }
        _skipPrevious = Builder(name = "SkipPrevious", defaultWidth = 48.0.dp, defaultHeight =
                48.0.dp, viewportWidth = 48.0f, viewportHeight = 48.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(12.0f, 12.0f)
                horizontalLineToRelative(4.0f)
                verticalLineToRelative(24.0f)
                horizontalLineToRelative(-4.0f)
                close()
                moveTo(19.0f, 24.0f)
                lineToRelative(17.0f, 12.0f)
                lineTo(36.0f, 12.0f)
                close()
            }
        }
        .build()
        return _skipPrevious!!
    }

private var _skipPrevious: ImageVector? = null
