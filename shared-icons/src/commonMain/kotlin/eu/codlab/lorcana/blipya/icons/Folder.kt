@file:Suppress("ktlint")
package eu.codlab.lorcana.blipya.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Rounded.Folder: ImageVector
    get() {
        if (_folder != null) {
            return _folder!!
        }
        _folder = materialIcon(name = "Rounded.Folder") {
            materialPath {
                moveTo(10.59f, 4.59f)
                curveTo(10.21f, 4.21f, 9.7f, 4.0f, 9.17f, 4.0f)
                horizontalLineTo(4.0f)
                curveToRelative(-1.1f, 0.0f, -1.99f, 0.9f, -1.99f, 2.0f)
                lineTo(2.0f, 18.0f)
                curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
                horizontalLineToRelative(16.0f)
                curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
                verticalLineTo(8.0f)
                curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                horizontalLineToRelative(-8.0f)
                lineToRelative(-1.41f, -1.41f)
                close()
            }
        }
        return _folder!!
    }

private var _folder: ImageVector? = null
