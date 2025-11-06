package eu.codlab.blipya

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import eu.codlab.lorcana.blipya.home.App
import eu.codlab.lorcana.blipya.home.AppBackPressProvider
import eu.codlab.permissions.PermissionsController

class MainActivity : FragmentActivity() {
    private val onBackPressProvider = AppBackPressProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PermissionsController.setActivity(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Box {
                App(
                    isDarkTheme = isSystemInDarkTheme(),
                    onBackPressed = onBackPressProvider,
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!onBackPressProvider.onBackPress()) {
            super.onBackPressed()
        }
    }
}
