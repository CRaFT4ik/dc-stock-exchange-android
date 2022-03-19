package ru.er_log.stock.android.base

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.er_log.stock.android.base.utils.StockMessage
import ru.er_log.stock.android.compose.components.StockBottomBar
import ru.er_log.stock.android.compose.components.StockScaffold
import ru.er_log.stock.android.compose.theme.StockTheme
import ru.er_log.stock.android.compose.theme.darkColors

@Composable
fun StartScreen() {
    StockTheme(colors = darkColors()) {
        val appState = rememberAppState()
        StockScaffold(
            scaffoldState = appState.scaffoldState,
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

        LaunchedEffect(StockMessage) {
            StockMessage.appErrors.collect {
                it.message?.let { errorText ->
                    appState.scaffoldState.snackbarHostState.showSnackbar(
                        errorText,
                        "OK",
                        SnackbarDuration.Indefinite
                    )
                }
            }
        }
    }
}