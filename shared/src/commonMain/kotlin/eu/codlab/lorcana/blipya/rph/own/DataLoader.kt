package eu.codlab.lorcana.blipya.rph.own

import korlibs.time.DateTime

sealed class DataLoader<T>(protected val id: Long) {
    fun isSame(loader: DataLoader<T>) = loader.id == id

    class Loading<T> : DataLoader<T>(DateTime.nowUnixMillisLong())

    class Loaded<T>(loading: DataLoader<T>, val data: T) : DataLoader<T>(loading.id)

    class Error<T>(previous: DataLoader<T>, val throwable: Throwable) : DataLoader<T>(previous.id)
}
