package ru.er_log.stock.android.compose.components

import android.content.Context
import android.content.res.Resources
import android.view.KeyEvent
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.er_log.stock.android.R
import ru.er_log.stock.android.compose.theme.AppTheme
import java.util.regex.Pattern

@Composable
internal fun AppTextField(
    modifier: Modifier = Modifier,
    state: AppTextFieldState = AppTextFieldState(),
    @StringRes label: Int? = null,
    isPasswordField: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
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
        value = state.field.value,
        isError = state.error.value != null,
        onValueChange = { state.onValueChange(it, localContext, composableScope) },
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

    state.error.value?.let { errorText ->
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

open class AppTextFieldState {

    val field = mutableStateOf(TextFieldValue())
    val error = mutableStateOf<String?>(null)

    private val textWatcher = TextWatcher()

    open fun validateInput(input: String): Boolean = true
    open fun formErrorMessage(resources: Resources, input: String): String = ""

    fun onValueChange(changedValue: TextFieldValue, context: Context, scope: CoroutineScope) {
        val input = changedValue.text
        field.apply { value = changedValue }

        textWatcher.update(input.length, scope) {
            if (!validateInput(input)) {
                error.value = formErrorMessage(context.resources, input)
            } else {
                error.value = null
            }
        }
    }

    private class TextWatcher(
        private val delay: Long = 600
    ) {
        private var currentJob: Job = Job()

        fun update(inputLength: Int, scope: CoroutineScope, updateAction: () -> Unit) {
            currentJob.cancel()
            currentJob = scope.launch {
                delay(delay)
                updateAction()
            }
        }
    }
}

internal class AppPasswordTextFieldState : AppTextFieldState() {

    // Minimum eight characters, at least one letter, one number and one special character.
    private val passwordRequiredPattern =
        "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"

    override fun validateInput(input: String): Boolean {
        return Pattern.matches(passwordRequiredPattern, input)
    }

    override fun formErrorMessage(resources: Resources, input: String): String {
        return resources.getString(R.string.auth_register_error_wrong_password)
    }
}

internal class AppLoginTextFieldState : AppTextFieldState() {

    override fun validateInput(input: String): Boolean {
        return input.length > 5
    }

    override fun formErrorMessage(resources: Resources, input: String): String {
        return resources.getString(R.string.auth_register_error_wrong_login)
    }
}