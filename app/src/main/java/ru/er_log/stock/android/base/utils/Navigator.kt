package ru.er_log.stock.android.base.utils

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Navigator {

    private val _sharedFlow = MutableSharedFlow<NavTarget>(extraBufferCapacity = 1)
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun navigateTo(navTarget: NavTarget) {
        _sharedFlow.tryEmit(navTarget)
    }

    val builder = NavOptionsBuilder().apply {
        this.launchSingleTop
    }

    enum class NavTarget(
        val route: String,
        val navOptions: NavOptionsBuilder.() -> Unit = {}
    ) {
        AuthHome(
            route = MainDestinations.AUTH_ROUTE + "/home",
            navOptions = { launchSingleTop = true }
        ),

        AuthLogin(
            route = MainDestinations.AUTH_ROUTE + "/login"
        ),

        HomeFeed(
            route = MainDestinations.HOME_ROUTE + "/feed"
        )
    }

    object MainDestinations {
        const val AUTH_ROUTE = "auth"
        const val HOME_ROUTE = "home"
    }
}