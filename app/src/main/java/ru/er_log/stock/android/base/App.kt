package ru.er_log.stock.android.base

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.Lifecycle
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel
import ru.er_log.stock.android.base.utils.Navigator
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.android.compose.theme.darkColors
import ru.er_log.stock.android.features.auth.ProfileViewModel
import ru.er_log.stock.android.features.auth.login.ScreenLogin
import ru.er_log.stock.android.features.home.HomeScreen

@Composable
fun App(
    navigator: Navigator = Navigator(),
    profileViewModel: ProfileViewModel = getViewModel()
) {
    AppTheme(colors = darkColors()) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Navigator.MainDestinations.AUTH_ROUTE
        ) {
            navigation(
                route = Navigator.MainDestinations.AUTH_ROUTE,
                startDestination = Navigator.NavTarget.AuthLogin.route
            ) {
                composable(Navigator.NavTarget.AuthLogin.route) {
                    ScreenLogin(navigator)
                }
            }

            navigation(
                route = Navigator.MainDestinations.HOME_ROUTE,
                startDestination = Navigator.NavTarget.AuthLogin.route
            ) {
                composable(Navigator.NavTarget.HomeFeed.route) {
                    HomeScreen(navigator)
                }
            }
        }

        // Sets up navigation events handling.
        LaunchedEffect("navigation") {
            navigator.sharedFlow.onEach {
                navController.navigate(it.route, it.navOptions)
            }.launchIn(this)
        }

        // Monitors account sign in state.
        if (profileViewModel.profile.observeAsState().value == null) {
            navigator.navigateTo(Navigator.NavTarget.AuthLogin)
        }
    }
}

//@Composable
//fun rememberAppState(
//    scaffoldState: ScaffoldState = rememberScaffoldState(),
//    navController: NavHostController = rememberNavController()
//) {
//    return remember(scaffoldState, navController) {
//        AppState(scaffoldState, navController)
//    }
//}

@Stable
class AppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController
) {
    // ----------------------------------------------------------
    // Источник состояния BottomBar
    // ----------------------------------------------------------

//    val bottomBarTabs = HomeSections.values()
//    private val bottomBarRoutes = bottomBarTabs.map { it.route }
//
//    // Атрибут отображения навигационного меню bottomBar
//    val shouldShowBottomBar: Boolean
//        @Composable get() = navController
//            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

    // ----------------------------------------------------------
    // Источник состояния навигации
    // ----------------------------------------------------------

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    // Клик по навигационному меню, вкладке.
    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                //Возвращаем выбранный экран,
                //иначе если backstack не пустой то показываем ранее открытое состяние
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }
}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}
