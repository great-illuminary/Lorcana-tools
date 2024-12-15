package eu.codlab.lorcana.blipya.home

class AppBackPressProvider {
    private var onBackPressed: () -> Boolean = {
        false
    }

    fun registerOnBackPress(onBackPress: () -> Boolean) {
        onBackPressed = onBackPress
    }

    fun onBackPress() = onBackPressed()
}
