package ru.er_log.stock.android.features.account

import android.content.res.Resources
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.utils.onlyFalse
import ru.er_log.stock.android.compose.components.*
import ru.er_log.stock.domain.models.`in`.Lot
import java.math.BigDecimal

@Composable
fun LotCreationDialog(
    title: String? = stringResource(R.string.lot_creation_form_title),
    state: LotCreationState = rememberLotCreationDialogState(),
    onDismissRequest: () -> Unit,
    onCreateAction: (Lot) -> Unit
) {
    StockDialog(
        title = title,
        onDismissRequest = onDismissRequest
    ) {
        DialogContent(
            state = state,
            onDismissRequest = onDismissRequest,
            onCreateAction = onCreateAction
        )
    }
}

@Composable
private fun DialogContent(
    state: LotCreationState = rememberLotCreationDialogState(),
    onDismissRequest: () -> Unit,
    onCreateAction: (Lot) -> Unit
) {
    val inputValidator = remember { LotCreationInputValidator() }

    val isFieldsOkay = derivedStateOf {
        state.priceInputState.hasError.onlyFalse() && state.amountInputState.hasError.onlyFalse()
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        StockTextField(
            isOutlined = true,
            inputState = state.priceInputState,
            inputValidator = inputValidator,
            label = R.string.lot_creation_form_price,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
        )
        Spacer(Modifier.size(3.dp))
        StockTextField(
            isOutlined = true,
            inputState = state.amountInputState,
            inputValidator = inputValidator,
            label = R.string.lot_creation_form_amount,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(Modifier.size(9.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StockOutlinedButton(
                enabled = isFieldsOkay.value,
                modifier = Modifier.weight(6f),
                onClick = {
                    val lotCreationRequest = Lot(
                        price = state.priceInputState.input.value.toBigDecimal(),
                        amount = state.amountInputState.input.value.toBigDecimal()
                    )
                    onCreateAction(lotCreationRequest)
                }
            ) {
                Text(stringResource(R.string.submit))
            }

            Spacer(modifier = Modifier.weight(5f))

            StockTextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    }
}

class LotCreationInputValidator : StockInputValidator() {
    override fun validateInput(input: String, resources: Resources): String? {
        val number = try {
            input.toBigDecimal()
        } catch (e: NumberFormatException) {
            return resources.getString(R.string.lot_creation_form_error_use_only_positive_digits)
        }

        if (number <= BigDecimal.ZERO) {
            return resources.getString(R.string.lot_creation_form_error_number_must_be_positive)
        }
        return null
    }
}

data class LotCreationState(
    val priceInputState: InputState,
    val amountInputState: InputState
) {
    class StateSaver : Saver<LotCreationState, List<InputState>> {
        override fun SaverScope.save(value: LotCreationState): List<InputState> {
            return listOf(value.priceInputState, value.amountInputState)
        }

        override fun restore(value: List<InputState>): LotCreationState {
            val (v1, v2) = value
            return LotCreationState(v1, v2)
        }
    }
}

@Composable
fun rememberLotCreationDialogState(
    priceInputState: InputState = rememberSaveable { InputState() },
    amountInputState: InputState = rememberSaveable { InputState() },
): LotCreationState {
    return rememberSaveable(
        priceInputState, amountInputState,
        saver = LotCreationState.StateSaver()
    ) {
        LotCreationState(priceInputState, amountInputState)
    }
}

@Preview
@Composable
private fun LotCreationDialogPreview() {
    DialogContent(
        state = rememberLotCreationDialogState(),
        onDismissRequest = {}, onCreateAction = {}
    )
}