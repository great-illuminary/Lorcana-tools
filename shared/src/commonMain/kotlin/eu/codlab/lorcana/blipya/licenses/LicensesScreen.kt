package eu.codlab.lorcana.blipya.licenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.codlab.compose.widgets.StatusBarAndNavigation
import eu.codlab.lorcana.blipya.home.AppModel

@Composable
fun LicensesScreen(
    modifier: Modifier,
    model: AppModel
) {
    StatusBarAndNavigation()

    Column(
        modifier = modifier,
        Arrangement.spacedBy(5.dp)
    ) {
        LicensesContent(model)
    }
}
