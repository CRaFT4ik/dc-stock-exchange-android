package ru.er_log.stock.android.features.home.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.er_log.stock.android.compose.components.AppSurface
import ru.er_log.stock.android.compose.components.AppTextField
import ru.er_log.stock.domain.models.exchange.Lot

@Composable
fun LotCreationDialog(
    onCreateLot: (Lot) -> Unit
) {
    val (dialogVisible, shouldShowDialog) = remember { mutableStateOf(true) }
    if (!dialogVisible) return

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        AppTextField(

        )

        AppSurface(
            modifier = Modifier
                .padding(40.dp)
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Hello")
        }
    }
}