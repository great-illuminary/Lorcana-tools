package eu.codlab.dispatchers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual val DefaultDispatcher = Dispatchers.IO
