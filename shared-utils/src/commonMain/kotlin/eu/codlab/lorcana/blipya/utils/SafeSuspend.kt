package eu.codlab.lorcana.blipya.utils

@Suppress("TooGenericExceptionCaught")
suspend fun <T> safeSuspend(
    onError: suspend (Throwable) -> T? = { null },
    execute: suspend () -> T
): T? = try {
    execute()
} catch (e: Throwable) {
    onError(e)
}
