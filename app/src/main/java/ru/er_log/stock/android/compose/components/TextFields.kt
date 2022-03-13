package ru.er_log.stock.android.compose.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import android.view.KeyEvent
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.er_log.stock.android.compose.theme.AppTheme

private val textFieldColors: TextFieldColors
    @Composable get() = TextFieldDefaults.textFieldColors(
        textColor = AppTheme.colors.textPrimary,
        backgroundColor = AppTheme.colors.surface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedLabelColor = AppTheme.colors.textSecondary,
        unfocusedLabelColor = AppTheme.colors.textSecondary,
        cursorColor = AppTheme.colors.textPrimary,
        errorIndicatorColor = AppTheme.colors.error,
        errorLabelColor = AppTheme.colors.error,
        errorCursorColor = AppTheme.colors.error
    )

private val outlinedTextFieldColors: TextFieldColors
    @Composable get() = TextFieldDefaults.outlinedTextFieldColors(
        textColor = AppTheme.colors.textPrimary,
        backgroundColor = Color.Transparent,
        focusedBorderColor = AppTheme.colors.onSurfaceSecondary,
        unfocusedBorderColor = AppTheme.colors.onSurface,
        focusedLabelColor = AppTheme.colors.textSecondary,
        unfocusedLabelColor = AppTheme.colors.textSecondary,
        cursorColor = AppTheme.colors.textPrimary,
        errorLabelColor = AppTheme.colors.error,
        errorCursorColor = AppTheme.colors.error,
        errorBorderColor = AppTheme.colors.error
    )

@Composable
internal fun AppTextField(
    modifier: Modifier = Modifier,
    isOutlined: Boolean = false,
    inputState: InputState = InputState(),
    inputValidator: AppInputValidator? = null,
    inputFilter: InputFilter? = InputFilter.WhitespaceInputFilter(),
    @StringRes label: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    colors: TextFieldColors = if (!isOutlined) textFieldColors else outlinedTextFieldColors
) {
    val stateField = rememberSaveable { inputState }
    val isPasswordHidden = remember { mutableStateOf(true) }
    val isPasswordField = when (keyboardOptions.keyboardType) {
        KeyboardType.Password -> true
        KeyboardType.NumberPassword -> true
        else -> false
    }

    val composableScope = rememberCoroutineScope()
    val localContext = LocalContext.current
    val localFocusManager = LocalFocusManager.current
    val localTextInputService = LocalTextInputService.current

    Column(
        modifier = modifier
    ) {
        AppTextFieldImpl(
            isOutlined = isOutlined,
            modifier = Modifier
                .fillMaxWidth()
                .onKeyEvent {
                    /** This is only for hardware keyboard (not on-screen keyboard).
                     * [keyboardOptions] and [keyboardActions] not applicable here yet. */
                    when (it.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_TAB -> {
                            if (keyboardActions.onNext != null) {
                                localFocusManager.moveFocus(FocusDirection.Next)
                            } else {
                                localFocusManager.clearFocus(true)
                            }
                            return@onKeyEvent true
                        }
                        KeyEvent.KEYCODE_ENTER -> {
                            if (keyboardActions.onDone != null) {
                                localFocusManager.clearFocus(true)
                                localTextInputService?.hideSoftwareKeyboard()
                            }
                            return@onKeyEvent true
                        }
                    }
                    return@onKeyEvent false
                },
            value = stateField.input.value,
            isError = stateField.error.value != null,
            onValueChange = { raw ->
                val filtered = inputFilter?.filter(raw) ?: raw
                stateField.input.value = filtered
                inputValidator?.validate(stateField, localContext, composableScope)
            },
            label = { label?.let { Text(stringResource(it)) } },
            colors = colors,
            trailingIcon = {
                if (isPasswordField) {
                    IconButton(onClick = { isPasswordHidden.value = !isPasswordHidden.value }) {
                        Icon(
                            imageVector = if (!isPasswordHidden.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Show/Hide password",
                            tint = AppTheme.colors.surfaceSecondary
                        )
                    }
                }
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = when (isPasswordField) {
                true -> keyboardOptions.copy(keyboardType = KeyboardType.Password)
                else -> keyboardOptions
            },
            keyboardActions = keyboardActions,
            shape = RoundedCornerShape(4.dp),
            visualTransformation = when (isPasswordField && isPasswordHidden.value) {
                true -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            }
        )

        val errorText = stateField.error.value
        AnimatedVisibility(visible = errorText?.length ?: 0 > 0) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                text = errorText ?: "",
                style = TextStyle(
                    color = AppTheme.colors.error,
                    fontSize = AppTheme.typography.caption.fontSize
                )
            )
        }
    }
}

@Composable
private fun AppTextFieldImpl(
    isOutlined: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    shape: Shape =
        MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
) {
    when (isOutlined) {
        true -> OutlinedTextField(
            modifier = modifier,
            value = value,
            isError = isError,
            onValueChange = onValueChange,
            label = label,
            colors = colors,
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = shape,
            visualTransformation = visualTransformation
        )
        else -> TextField(
            modifier = modifier,
            value = value,
            isError = isError,
            onValueChange = onValueChange,
            label = label,
            colors = colors,
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = shape,
            visualTransformation = visualTransformation
        )
    }
}

open class InputState : Parcelable {
    val input = mutableStateOf("")
    val error = mutableStateOf<String?>(null)

    // null - initial value (state is unknown)
    val hasErrorState = mutableStateOf<Boolean?>(null)
    val hasError: Boolean?
        get() = hasErrorState.value

    @SuppressLint("ParcelCreator")
    companion object CREATOR : Parcelable.Creator<InputState> {
        override fun createFromParcel(parcel: Parcel): InputState {
            return InputState().apply {
                parcel.readString()?.let { input.value = it }
                parcel.readString()?.let { error.value = it }
            }
        }

        override fun newArray(size: Int): Array<InputState?> = arrayOfNulls(size)
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(input.value)
        dest?.writeString(error.value)
    }

    override fun describeContents(): Int = 0
}

abstract class InputFilter {
    abstract fun filter(input: String): String

    class WhitespaceInputFilter : InputFilter() {
        override fun filter(input: String): String {
            return input.filter { !it.isWhitespace() }
        }
    }
}

abstract class AppInputValidator {

    private var updateErrorJob: Job? = null

    /**
     * @param input input text
     * @return error string or null
     */
    abstract fun validateInput(input: String, resources: Resources): String?

    fun validate(
        inputState: InputState,
        context: Context,
        scope: CoroutineScope,
        delayMs: Long = 650
    ) {
        val input = inputState.input.value
        if (input.isEmpty()) {
            inputState.hasErrorState.value = null
            updateErrorText(null, inputState, scope, delayMs)
        } else {
            validateInput(input, context.resources).let { error ->
                val hasError = error != null
                inputState.hasErrorState.value = hasError
                updateErrorText(error, inputState, scope, delayMs)
            }
        }
    }

    private fun updateErrorText(
        error: String?,
        inputState: InputState,
        scope: CoroutineScope,
        delayMs: Long = 650
    ) {
        updateErrorJob?.cancel()
        updateErrorJob = scope.launch {
            delay(delayMs)
            inputState.error.value = error
        }
    }
}