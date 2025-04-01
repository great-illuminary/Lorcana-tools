package eu.codlab.lorcana.blipya.utils

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import eu.codlab.sentry.wrapper.Sentry
import eu.codlab.viewmodel.launch
import kotlinx.coroutines.CoroutineScope

fun ViewModel.safeLaunch(
    onError: (Throwable) -> Unit = {
        Sentry.captureException(it)
    },
    run: suspend CoroutineScope.() -> Unit
) = launch(onError, run)
