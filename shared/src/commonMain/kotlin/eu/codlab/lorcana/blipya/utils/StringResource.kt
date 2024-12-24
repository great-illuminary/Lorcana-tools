package eu.codlab.lorcana.blipya.utils

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StringResource.localized() = stringResource(this)