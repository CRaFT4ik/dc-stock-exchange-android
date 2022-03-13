package ru.er_log.stock.android.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import org.koin.androidx.compose.getViewModel
import ru.er_log.stock.android.base.utils.Navigator
import ru.er_log.stock.android.base.utils.rememberNavigator
import ru.er_log.stock.android.compose.theme.StockTheme
import ru.er_log.stock.android.compose.theme.darkColors
import ru.er_log.stock.android.features.auth.AuthViewModel
import ru.er_log.stock.android.features.auth.login.ScreenLogin
import ru.er_log.stock.android.features.home.HomeScreen

@Composable
fun App(
    authViewModel: AuthViewModel = getViewModel()
) {
    val navController = rememberNavController()
    val authNavigator = rememberNavigator(navController)

    StockTheme(colors = darkColors()) {
        NavHost(
            navController = navController,
            startDestination = Navigator.MainDestinations.AUTH_ROUTE // TODO: add if user logged in check
        ) {
            authNavGraph(authNavigator)
        }

        // Monitors account sign in state.
        if (!authViewModel.isUserLoggedIn.collectAsState().value) {
            authNavigator.navigateTo(Navigator.NavTarget.AuthLogin)
        } else {  // TODO: remove after
            authNavigator.navigateTo(Navigator.NavTarget.HomeAccount)
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
        startDestination = Navigator.NavTarget.HomeAccount.route
    ) {
        composable(Navigator.NavTarget.HomeAccount.route) {
            HomeScreen()
        }
    }
}