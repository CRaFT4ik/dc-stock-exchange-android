package ru.er_log.stock.android.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import org.koin.androidx.compose.getViewModel
import ru.er_log.stock.android.base.utils.Navigator
import ru.er_log.stock.android.base.utils.observe
import ru.er_log.stock.android.base.utils.rememberNavigator
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.android.compose.theme.darkColors
import ru.er_log.stock.android.features.auth.ProfileViewModel
import ru.er_log.stock.android.features.auth.login.ScreenLogin
import ru.er_log.stock.android.features.home.HomeScreen

@Composable
fun App(
    profileViewModel: ProfileViewModel = getViewModel()
) {
    val navController = rememberNavController()
    val authNavigator = rememberNavigator(navController)

    AppTheme(colors = darkColors()) {
        NavHost(
            navController = navController,
            startDestination = Navigator.MainDestinations.AUTH_ROUTE // TODO: add if user logged in check
        ) {
            authNavGraph(authNavigator)
        }

        // Monitors account sign in state.
        if (profileViewModel.profile.observeAsState().value == null) {
            authNavigator.navigateTo(Navigator.NavTarget.AuthLogin)
        } else {  // TODO: remove after
            authNavigator.navigateTo(Navigator.NavTarget.HomeFeed)
        }
    }
}

fun NavGraphBuilder.authNavGraph(navigator: Navigator) {
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
        startDestination = Navigator.NavTarget.HomeFeed.route
    ) {
        composable(Navigator.NavTarget.HomeFeed.route) {
            HomeScreen()
        }
    }
}