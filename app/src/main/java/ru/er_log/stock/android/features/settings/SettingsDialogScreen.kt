package ru.er_log.stock.android.features.settings

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import ru.er_log.stock.android.R
import ru.er_log.stock.android.compose.components.StockDialog
import ru.er_log.stock.android.compose.components.StockOutlinedButton
import ru.er_log.stock.android.compose.components.StockSwitch
import ru.er_log.stock.android.compose.components.StockTextButton

@Composable
fun SettingsDialogScreen(
    onDismissRequest: () -> Unit,
    settingsViewModel: SettingsViewModel = getViewModel()
) {
    StockDialog(
        title = stringResource(R.string.settings_section_title),
        onDismissRequest = onDismissRequest
    ) {
        DialogContent(
            onDismissRequest = onDismissRequest,
            isLightTheme = settingsViewModel.isLightTheme.value,
            onChangeTheme = settingsViewModel::setAppTheme,
            onLogout = settingsViewModel::logout
        )
    }
}

@Composable
private fun DialogContent(
    onDismissRequest: () -> Unit,
    isLightTheme: Boolean,
    onChangeTheme: (Boolean) -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.settings_switch_theme_title))
            Spacer(Modifier.size(12.dp))
            AppThemeSwitch(
                isLightTheme = isLightTheme,
                onCheckedChange = onChangeTheme
            )
        }

        Spacer(Modifier.size(12.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            StockOutlinedButton(
                modifier = Modifier.weight(2f, true),
                onClick = onLogout
            ) {
                Text(stringResource(R.string.settings_button_logout))
            }
            Spacer(Modifier.weight(1f, true))
            StockTextButton(
                modifier = Modifier.weight(1f),
                onClick = onDismissRequest
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    }
}

@Composable
private fun AppThemeSwitch(
    isLightTheme: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val colorSun = Color(0xFFDAB75B)
    val colorSunBack = Color(0xFFF8DC6E)
    val colorSunSky = Color(0xFF73BDBB)
    val colorMoon = Color(0xFFFEFEFF)
    val colorMoonBack = Color(0xFF7D79D3)
    val colorMoonSky = Color(0xFF4D46BF)

    StockSwitch(
        initial = isLightTheme,
        modifier = Modifier
            .size(60.dp, 30.dp)
            .clip(RoundedCornerShape(percent = 50)),
        onCheckedChange = onCheckedChange,
        switchOffBackground = colorMoonSky,
        switchOnBackground = colorSunSky,
        switchAnimSpec = spring(),
        backgroundAnimSpec = tween(800)
    ) { isLightSelected ->
        AppThemeSwitchIcon(
            modifier = Modifier.clip(CircleShape),
            icon = if (isLightSelected) null else Icons.Default.DarkMode,
            iconColor = if (isLightSelected) colorSun else colorMoon,
            backgroundColor = if (isLightSelected) colorSunBack else colorMoonBack,
        )
    }
}

@Composable
private fun AppThemeSwitchIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector?,
    iconColor: Color,
    backgroundColor: Color
) {
    val iconColorAnim by animateColorAsState(iconColor, tween(500))
    val backgroundAnim by animateColorAsState(backgroundColor, tween(500))

    Box(
        modifier = Modifier
            .padding(3.dp)
            .then(modifier)
            .fillMaxSize()
            .background(backgroundAnim)
            .border(2.dp, iconColorAnim, CircleShape)
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(targetState = icon, animationSpec = tween(500)) {
            it?.let {
                Icon(imageVector = it, contentDescription = "Switch", tint = iconColorAnim)
            }
        }
    }
}

@Preview
@Composable
private fun AppThemeSwitchPreview() {
    AppThemeSwitch(false) {}
}
