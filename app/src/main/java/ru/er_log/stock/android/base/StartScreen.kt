package ru.er_log.stock.android.base

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import ru.er_log.stock.android.base.utils.UserPrefsStore
import ru.er_log.stock.android.base.utils.StockMessage
import ru.er_log.stock.android.compose.components.StockBottomBar
import ru.er_log.stock.android.compose.components.StockScaffold
import ru.er_log.stock.android.compose.theme.StockTheme
import ru.er_log.stock.android.compose.theme.darkColors
import ru.er_log.stock.android.compose.theme.lightColors
import ru.er_log.stock.data.di.inject

@Composable
fun StartScreen(
    isLightTheme: Boolean
) {
    StockTheme(colors = if (!isLightTheme) darkColors() else lightColors()) {
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

//      TODO: provide correct way to handle common errors
//        LaunchedEffect(StockMessage) {
//            StockMessage.appErrors.collect {
//                it.message?.let { errorText ->
//                    appState.scaffoldState.snackbarHostState.showSnackbar(
//                        errorText,
//                        "OK",
//                        SnackbarDuration.Indefinite
//                    )
//                }
//            }
//        }
    }
}