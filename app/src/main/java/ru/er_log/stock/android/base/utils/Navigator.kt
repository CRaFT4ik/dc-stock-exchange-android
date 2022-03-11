package ru.er_log.stock.android.base.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState

class Navigator(
    private val navController: NavHostController
) {
    val currentTarget: NavTarget?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination?.let {
            NavTarget.values().firstOrNull { t -> it.route?.contains(t.route) ?: false }
        }

    fun navigateTo(navTarget: NavTarget) {
        navController.navigate(navTarget.route, navTarget.navOptions)
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
        ),
        OrderBook(
            route = MainDestinations.HOME_ROUTE + "/order_book"
        ),
        Profile(
            route = MainDestinations.HOME_ROUTE + "/profile"
        )
    }

    object MainDestinations {
        const val AUTH_ROUTE = "auth"
        const val HOME_ROUTE = "home"
    }
}

/**
 * Sets up navigation events handling.
 */
@Composable
fun Navigator.observe(navController: NavHostController) {
//    LaunchedEffect("navigation") {
//        this@observe.sharedFlow.onEach {
//            navController.navigate(it.route, it.navOptions)
//        }.launchIn(this)
//    }
}

@Composable
fun rememberNavigator(navController: NavHostController): Navigator {
    return remember { Navigator(navController) }
}
