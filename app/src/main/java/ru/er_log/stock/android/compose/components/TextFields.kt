package ru.er_log.stock.android.compose.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import android.view.KeyEvent
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
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
import ru.er_log.stock.android.R
import ru.er_log.stock.android.compose.theme.AppTheme

@Composable
internal fun AppTextField(
    modifier: Modifier = Modifier,
    inputState: InputState = InputState(),
    inputValidator: AppInputValidator? = null,
    inputFilter: InputFilter? = InputFilter.WhitespaceInputFilter(),
    @StringRes label: Int? = null,
    isPasswordField: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    val stateField = rememberSaveable { inputState }
    val hidePassword = remember { mutableStateOf(true) }

    val composableScope = rememberCoroutineScope()
    val localContext = LocalContext.current
    val localFocusManager = LocalFocusManager.current
    val localTextInputService = LocalTextInputService.current

    TextField(
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
            }
            .then(modifier),
        value = stateField.input.value,
        isError = stateField.error.value != null,
        onValueChange = { raw ->
            val filtered = inputFilter?.filter(raw) ?: raw
            stateField.input.value = filtered
            inputValidator?.validateDelayed(stateField, localContext, composableScope)
        },
        label = { if (label != null) Text(stringResource(label)) },
        colors = TextFieldDefaults.textFieldColors(
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
        ),
        trailingIcon = {
            if (isPasswordField) {
                Icon(
                    modifier = Modifier
                        .clickable { hidePassword.value = !hidePassword.value },
                    painter = if (hidePassword.value) {
                        painterResource(R.drawable.ic_baseline_visibility_24)
                    } else {
                        painterResource(R.drawable.ic_baseline_visibility_off_24)
                    },
                    contentDescription = "Show/Hide password",
                    tint = AppTheme.colors.surfaceSecondary
                )
            }
        },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = if (isPasswordField) {
            keyboardOptions.copy(keyboardType = KeyboardType.Password)
        } else {
            keyboardOptions
        },
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(4.dp),
        visualTransformation = if (isPasswordField && hidePassword.value) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }
    )

    stateField.error.value?.let { errorText ->
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            text = errorText,
            style = TextStyle(
                color = AppTheme.colors.error,
                fontSize = AppTheme.typography.caption.fontSize
            )
        )
    }
}

open class InputState : Parcelable {
    val input = mutableStateOf("")
    val error = mutableStateOf<String?>(null)

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

    abstract fun validateInput(input: String): Boolean
    abstract fun formErrorMessage(resources: Resources, input: String): String

    fun validate(inputState: InputState, context: Context): Boolean {
        val input = inputState.input.value
        if (!validateInput(input)) {
            inputState.error.value = formErrorMessage(context.resources, input)
            return false
        }

        inputState.error.value = null
        return true
    }

    fun validateDelayed(
        inputState: InputState,
        context: Context,
        scope: CoroutineScope,
        delayMs: Long = 450
    ) {
        updateErrorJob?.cancel()
        updateErrorJob = scope.launch {
            delay(delayMs)
            validate(inputState, context)
        }
    }
}