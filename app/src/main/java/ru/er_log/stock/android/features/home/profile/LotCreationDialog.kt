package ru.er_log.stock.android.features.home.profile

import android.content.res.Resources
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.domain.api.v1.exchange.LotCreationRequest
import java.math.BigDecimal

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LotCreationDialog(
    state: LotCreationState = rememberLotCreationDialogState(),
    onDismissRequest: () -> Unit,
    onCreateAction: (LotCreationRequest) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
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
    onCreateAction: (LotCreationRequest) -> Unit
) {
    AppSurface(
        modifier = Modifier.padding(24.dp),
        color = AppTheme.colors.surface,
        shape = RoundedCornerShape(6.dp)
    ) {
        val elemModifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        val inputValidator = remember { LotCreationInputValidator() }

        val isFieldsOkay = derivedStateOf {
            state.priceInputState.hasError.onlyFalse() && state.amountInputState.hasError.onlyFalse()
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AppTextField(
                isOutlined = true,
                inputState = state.priceInputState,
                inputValidator = inputValidator,
                modifier = elemModifier,
                label = R.string.lot_creation_form_price,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
            )

            AppTextField(
                isOutlined = true,
                inputState = state.amountInputState,
                inputValidator = inputValidator,
                modifier = elemModifier,
                label = R.string.lot_creation_form_amount,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                )
            )

            Row(
                modifier = elemModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppOutlinedButton(
                    enabled = isFieldsOkay.value,
                    modifier = Modifier.weight(6f),
                    onClick = {
                        val lotCreationRequest = LotCreationRequest(
                            price = state.priceInputState.input.value.toBigDecimal(),
                            amount = state.amountInputState.input.value.toBigDecimal()
                        )
                        onCreateAction(lotCreationRequest)
                    }
                ) {
                    Text(stringResource(R.string.submit))
                }

                Spacer(modifier = Modifier.weight(5f))

                AppTextButton(
                    onClick = { onDismissRequest() }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}

class LotCreationInputValidator : AppInputValidator() {
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