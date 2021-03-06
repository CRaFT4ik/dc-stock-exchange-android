package ru.er_log.stock.android.base

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.er_log.stock.android.R
import ru.er_log.stock.android.features.account.ProfileScreen
import ru.er_log.stock.android.features.auth.login.LoginScreen
import ru.er_log.stock.android.features.order_book.OrderBookScreen
import ru.er_log.stock.data.di.inject
import ru.er_log.stock.domain.usecases.AuthUseCases

@Composable
fun StockNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = AuthDestinations.Login.route,
        modifier = modifier
    ) {
        composable(AuthDestinations.Login.route) {
            LoginScreen(actionLoginSuccess = {
                navController.popBackStack()
                navController.navigate(HomeDestinations.startDestination.route)
            })
        }

        composable(HomeDestinations.OrderBook.route) {
            OrderBookScreen()
        }

        composable(HomeDestinations.Account.route) {
            ProfileScreen()
        }
    }

    // Globally observing login state. It works, but it should be framed in some other way.
    LaunchedEffect(key1 = navController) {
        inject<AuthUseCases>().observeLoginState(Unit, this) {
            it.onSuccess { userInfo ->
                if (userInfo == null) {
                    while (navController.popBackStack()) {}
                    navController.navigate(AuthDestinations.Login.route)
                }
            }
        }
    }
}

enum class AuthDestinations {
    Login;

    val route: String get() = "${Root.route}/$name"

    companion object Root {
        private const val route: String = "auth"
    }
}

enum class HomeDestinations(
    @StringRes val title: Int,
    val icon: ImageVector
) {
    OrderBook(R.string.nav_home_order_book, Icons.Outlined.TrendingUp),
    Account(R.string.nav_home_account, Icons.Outlined.PermIdentity);

    val route: String get() = "${Root.route}/$name"

    companion object Root {
        val startDestination = OrderBook
        private const val route: String = "home"
    }
}