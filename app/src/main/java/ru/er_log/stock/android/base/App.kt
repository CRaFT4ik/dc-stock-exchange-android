package ru.er_log.stock.android.base

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import ru.er_log.stock.android.compose.components.StockBottomBar
import ru.er_log.stock.android.compose.components.StockScaffold
import ru.er_log.stock.android.compose.theme.StockTheme
import ru.er_log.stock.android.compose.theme.darkColors
import ru.er_log.stock.android.features.auth.AuthViewModel

@Composable
fun App(
    authViewModel: AuthViewModel = getViewModel()
) {
    StockTheme(colors = darkColors()) {
        val appState = rememberAppState()
        StockScaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    StockBottomBar(
                        tabs = appState.bottomBarTabs,
                        currentRoute = appState.currentRoute!!,
                        actionNavigate = { appState.navigateToBottomBarRoute(it) }
                    )
                    Spacer(modifier = Modifier.padding(bottom = 50.dp))
                }
            }
        ) { innerPadding ->
            StockNavHost(
                navController = appState.navController,
                modifier = Modifier.padding(innerPadding)
            )
        }

        // Monitors account sign in state.
//        if (!authViewModel.isUserLoggedIn.collectAsState().value) {
//            appState.navController.navigate(AuthDestinations.Login.route)
//        } else {  // TODO: remove after
//            appState.navController.navigate(HomeDestinations.OrderBook.route)
//        }
    }
}