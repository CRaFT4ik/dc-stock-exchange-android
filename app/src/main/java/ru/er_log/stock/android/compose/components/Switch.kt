package ru.er_log.stock.android.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.er_log.stock.android.compose.theme.StockTheme

@Composable
fun StockSwitch(
    initial: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
    switchAnimSpec: AnimationSpec<Float> = spring(),
    backgroundAnimSpec: AnimationSpec<Color> = tween(250),
    switchOffBackground: Color = StockTheme.colors.onSurface,
    switchOnBackground: Color = StockTheme.colors.primary,
    switchItem: @Composable (isSelected: Boolean) -> Unit
) {
    val switchPosition = remember { Animatable(if (initial) 1f else 0f) }
    val (isChecked, setChecked) = remember { mutableStateOf(initial) }

    val scope = rememberCoroutineScope()
    val background by animateColorAsState(
        targetValue = if (!isChecked) switchOffBackground else switchOnBackground,
        animationSpec = backgroundAnimSpec
    )

    Layout(
        modifier = modifier
            .background(background)
            .size(height = 24.dp, width = 43.dp)
            .clickable {
                val checked = !isChecked
                scope.launch {
                    switchPosition.animateTo(if (checked) 1f else 0f, switchAnimSpec)
                }
                setChecked(checked)
                onCheckedChange(checked)
            },
        content = { Box(Modifier.layoutId("icon")) { switchItem(isChecked) } }
    ) { measurables, constraints ->

        val width = constraints.maxWidth
        val height = constraints.maxHeight

        val iconMeasurable = measurables.first { it.layoutId == "icon" }
        val iconPlaceable = iconMeasurable.measure(
            constraints.copy(
                minWidth = height, maxWidth = height,
                minHeight = height, maxHeight = height
            )
        )

        val maxX = width - iconPlaceable.measuredWidth
        layout(constraints.maxWidth, constraints.maxHeight) {
            val x = switchPosition.value * maxX
            iconPlaceable.placeRelative(x = x.toInt(), y = 0)
        }
    }
}

@Preview
@Composable
private fun StockSwitchPreview() {
    StockSwitch(
        modifier = Modifier.clip(CircleShape),
        initial = false
    ) {
        Icon(
            modifier = Modifier
                .padding(2.dp)
                .clip(CircleShape)
                .background(Color.Blue),
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Icon"
        )
    }
}
