package eu.codlab.lorcana.blipya.utils

@Suppress("TooGenericExceptionCaught")
fun <T> safeExecute(
    onError: (Throwable) -> T? = { null },
    execute: () -> T
): T? = try {
    execute()
} catch (e: Throwable) {
    onError(e)
}
