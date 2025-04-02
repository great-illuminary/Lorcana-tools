package eu.codlab.lorcana.blipya.utils

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import eu.codlab.sentry.wrapper.Sentry
import eu.codlab.viewmodel.launch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope

fun ViewModel.safeLaunch(
    onError: (Throwable) -> Unit = {
        if(it !is CancellationException) {
            Sentry.captureException(it)
        }
    },
    run: suspend CoroutineScope.() -> Unit
) = OriginalException().let { original ->
    launch(
        onError = { error ->
            error.addSuppressed(original)
            onError(error)
        },
        run
    )
}

class OriginalException : Exception()
