package eu.codlab.lorcana.blipya.deck.dreamborn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.DeckConfigurationModel
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground

@Composable
fun ShowDreambornInformation(
    modifier: Modifier,
    model: DeckConfigurationModel
) {
    val state by model.states.collectAsState()
    var prompt by remember { mutableStateOf(false) }

    val tintColor = if (LocalDarkTheme.current) {
        AppColor.White
    } else {
        AppColor.Black
    }

    PromptForDreambornUrl(
        showPrompt = prompt,
        onDismiss = {
            prompt = false
        },
        onConfirm = {
            prompt = false
            model.loadDreamborn(it)
        }
    )

    DefaultCard(
        modifier = modifier,
        backgroundColor = defaultCardBackground()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            TextNormal(
                text = "Dreamborn",
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))
            Divider(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))

            if (state.loadingDreamborn) {
                CircularProgressIndicator()
            } else {

                state.deck.dreamborn?.data?.let {
                    TextNormal(
                        text = "Name: ${it.name}"
                    )
                }

                Row {
                    IconButton(
                        onClick = {
                            prompt = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit ${state.name}",
                            tint = tintColor
                        )
                    }

                    state.deck.dreamborn?.data?.let {
                        val uriHandler = LocalUriHandler.current
                        IconButton(
                            onClick = {
                                model.openDreamborn(uriHandler)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Print,
                                contentDescription = "Print ${state.name}",
                                tint = tintColor
                            )
                        }
                    }
                }
            }
        }
    }
}