@file:Suppress("ktlint")
package eu.codlab.lorcana.blipya.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Filled.Print: ImageVector
    get() {
        if (_print != null) {
            return _print!!
        }
        _print = materialIcon(name = "Filled.Print") {
            materialPath {
                moveTo(19.0f, 8.0f)
                lineTo(5.0f, 8.0f)
                curveToRelative(-1.66f, 0.0f, -3.0f, 1.34f, -3.0f, 3.0f)
                verticalLineToRelative(6.0f)
                horizontalLineToRelative(4.0f)
                verticalLineToRelative(4.0f)
                horizontalLineToRelative(12.0f)
                verticalLineToRelative(-4.0f)
                horizontalLineToRelative(4.0f)
                verticalLineToRelative(-6.0f)
                curveToRelative(0.0f, -1.66f, -1.34f, -3.0f, -3.0f, -3.0f)
                close()
                moveTo(16.0f, 19.0f)
                lineTo(8.0f, 19.0f)
                verticalLineToRelative(-5.0f)
                horizontalLineToRelative(8.0f)
                verticalLineToRelative(5.0f)
                close()
                moveTo(19.0f, 12.0f)
                curveToRelative(-0.55f, 0.0f, -1.0f, -0.45f, -1.0f, -1.0f)
                reflectiveCurveToRelative(0.45f, -1.0f, 1.0f, -1.0f)
                reflectiveCurveToRelative(1.0f, 0.45f, 1.0f, 1.0f)
                reflectiveCurveToRelative(-0.45f, 1.0f, -1.0f, 1.0f)
                close()
                moveTo(18.0f, 3.0f)
                lineTo(6.0f, 3.0f)
                verticalLineToRelative(4.0f)
                horizontalLineToRelative(12.0f)
                lineTo(18.0f, 3.0f)
                close()
            }
        }
        return _print!!
    }

private var _print: ImageVector? = null
