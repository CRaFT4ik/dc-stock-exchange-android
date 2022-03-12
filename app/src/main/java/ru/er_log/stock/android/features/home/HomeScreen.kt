package ru.er_log.stock.android.features.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.utils.Navigator
import ru.er_log.stock.android.base.utils.rememberNavigator
import ru.er_log.stock.android.compose.components.AppBottomBar
import ru.er_log.stock.android.compose.components.AppBottomBarTab
import ru.er_log.stock.android.compose.components.AppScaffold
import ru.er_log.stock.android.compose.components.order_book.OrderBookPreviewProvider
import ru.er_log.stock.android.compose.components.order_book.OrderBookState
import ru.er_log.stock.android.features.home.order_book.OrderBookScreen
import ru.er_log.stock.android.features.home.profile.ProfileScreen

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val navigator = rememberNavigator(navController)

    AppScaffold(
        bottomBar = {
            val currentNavTarget = navigator.currentTarget
            if (currentNavTarget != null) {
                AppBottomBar(
                    tabs = homeButtonBar,
                    currentRoute = currentNavTarget,
                    actionNavigate = { navigator.navigateTo(it) }
                )
                Spacer(modifier = Modifier.padding(bottom = 50.dp))
            }
        }
    ) { paddings ->
        NavHost(
            modifier = Modifier.padding(paddings),
            navController = navController,
            startDestination = Navigator.MainDestinations.HOME_ROUTE
        ) {
            homeNavGraph(navigator)
        }
    }
}


fun NavGraphBuilder.homeNavGraph(navigator: Navigator) {
    navigation(
        route = Navigator.MainDestinations.HOME_ROUTE,
        startDestination = Navigator.NavTarget.HomeOrderBook.route
    ) {
        composable(Navigator.NavTarget.HomeOrderBook.route) {
            val orderBookState = remember {
                val provider = OrderBookPreviewProvider()
                OrderBookState(
                    mutableStateOf(provider.provide(10000, 20000)),
                    mutableStateOf(provider.provide(20000, 30000))
                )
            }

            OrderBookScreen(orderBookState)
        }

        composable(Navigator.NavTarget.HomeAccount.route) {
            ProfileScreen()
        }
    }
}

val homeButtonBar = arrayOf(
    AppBottomBarTab(
        R.string.nav_home_order_book,
        Icons.Outlined.TrendingUp,
        Navigator.NavTarget.HomeOrderBook
    ),
    AppBottomBarTab(
        R.string.nav_home_account,
        Icons.Outlined.PermIdentity,
        Navigator.NavTarget.HomeAccount
    )
)