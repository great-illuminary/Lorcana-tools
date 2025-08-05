package eu.codlab.lorcana.blipya.rph.own

import eu.codlab.http.createClient
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.rph.models.EventHolder
import eu.codlab.lorcana.blipya.rph.models.User
import eu.codlab.lorcana.blipya.save.RavensburgerPlayHubUser
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.viewmodel.StateViewModel
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.encodeURLParameter
import kotlinx.coroutines.flow.map

data class RphOwnRegistrationsState(
    val ravensburgerPlayHubUser: RavensburgerPlayHubUser? = null,
    val initialized: Boolean = false,
    val events: DataLoader<List<EventHolder>>? = null,
    val matchingUsers: DataLoader<List<User>>? = null
)

class RphOwnRegistrationsModel(
    private val appModel: AppModel
) : StateViewModel<RphOwnRegistrationsState>(
    RphOwnRegistrationsState(
        ravensburgerPlayHubUser = appModel.states.value.ravensburgerPlayHubUser
    )
) {
    private val client = createClient { }

    init {
        safeLaunch(
            onError = {
                it.printStackTrace()
            }
        ) {
            states.value.ravensburgerPlayHubUser?.let {
                startLoadingData(it)
            }

            updateState { copy(initialized = true) }

            appModel.states.map { it.ravensburgerPlayHubUser }.collect {
                updateState { copy(ravensburgerPlayHubUser = it) }

                it?.let { new -> startLoadingData(new) }
            }
        }
    }

    fun selectSpecificUser(user: User) = safeLaunch {
        appModel.saveRavensburgerPlayHubSelection(
            user.bestIdentifierInGame ?: user.bestIdentifier ?: "",
            user.id
        )
    }

    fun startMatchingUser(matching: String) = safeLaunch {
        val dataLoader = DataLoader.Loading<List<User>>()

        updateState {
            copy(
                matchingUsers = dataLoader
            )
        }

        val url = "https://api-lorcana.com/rph/users?matching=${matching.encodeURLParameter()}"

        val result = try {
            client.get(url)
        } catch (err: Throwable) {
            err.printStackTrace()
            null
        }
        try {
            val users = result?.body<List<User>>() ?: emptyList()

            // skipping
            states.value.matchingUsers?.let { current ->
                if (!current.isSame(dataLoader)) return@safeLaunch
            }

            updateState {
                copy(
                    matchingUsers = DataLoader.Loaded(dataLoader, users)
                )
            }
        } catch (err: Throwable) {
            err.printStackTrace()
        }
    }

    private fun startLoadingData(ravensburgerPlayHubUser: RavensburgerPlayHubUser) = safeLaunch {
        val dataLoader = DataLoader.Loading<List<EventHolder>>()

        updateState {
            copy(
                events = dataLoader
            )
        }

        val url = "https://api-lorcana.com/rph/user/${ravensburgerPlayHubUser.id}/events"

        val result = try {
            client.get(url)
        } catch (err: Throwable) {
            err.printStackTrace()
            null
        }

        val events = result?.body<List<EventHolder>>()
            ?.filter { null != it.latLng() } ?: emptyList()

        // skipping
        states.value.events?.let { current ->
            if (!current.isSame(dataLoader)) return@safeLaunch
        }

        updateState {
            copy(
                events = DataLoader.Loaded(dataLoader, events)
            )
        }
    }
}
