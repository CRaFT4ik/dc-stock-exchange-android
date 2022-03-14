package ru.er_log.stock.android.base

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.er_log.stock.android.R
import ru.er_log.stock.android.compose.components.order_book.OrderBookPreviewProvider
import ru.er_log.stock.android.compose.components.order_book.OrderBookState
import ru.er_log.stock.android.features.auth.login.LoginScreen
import ru.er_log.stock.android.features.home.order_book.OrderBookScreen
import ru.er_log.stock.android.features.home.profile.ProfileScreen

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
            val orderBookState = remember {
                val provider = OrderBookPreviewProvider()
                OrderBookState(
                    mutableStateOf(provider.provide(10000, 20000)),
                    mutableStateOf(provider.provide(20000, 30000))
                )
            }

            OrderBookScreen(orderBookState)
        }

        composable(HomeDestinations.Account.route) {
            ProfileScreen()
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