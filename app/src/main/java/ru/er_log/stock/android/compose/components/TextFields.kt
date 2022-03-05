package ru.er_log.stock.android.compose.components

import android.view.KeyEvent
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.er_log.stock.android.R
import ru.er_log.stock.android.compose.theme.AppTheme

@Composable
internal fun AppTextField(
    modifier: Modifier = Modifier,
    @StringRes label: Int? = null,
    isPasswordField: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: ((String) -> Unit)? = null
) {
    val state = remember { mutableStateOf(TextFieldValue()) }
    val hidePassword = remember { mutableStateOf(true) }

    val localFocusManager = LocalFocusManager.current
    val localTextInputService = LocalTextInputService.current

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .onKeyEvent {
                /** This is only for hardware keyboard (not on-screen keyboard).
                 * [keyboardOptions] and [keyboardActions] not applicable here yet. */
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
        value = state.value,
        onValueChange = {
            val trimmed = it.copy(it.text.trim())
            state.value = trimmed
            onValueChange?.invoke(trimmed.text)
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
            errorCursorColor = AppTheme.colors.error
        ),
        trailingIcon = {
            if (isPasswordField) {
                Icon(
                    modifier = Modifier
                        .focusable(false)
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
}