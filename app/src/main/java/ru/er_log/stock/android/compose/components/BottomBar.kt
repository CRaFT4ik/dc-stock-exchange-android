package ru.er_log.stock.android.compose.components

import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.util.lerp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import ru.er_log.stock.android.base.utils.Navigator
import ru.er_log.stock.android.compose.theme.StockTheme
import ru.er_log.stock.android.features.home.homeButtonBar

@Preview
@Composable
fun StockBottomBarPreview() {
    StockBottomBar(
        tabs = homeButtonBar,
        currentRoute = homeButtonBar[0].route,
        actionNavigate = {}
    )
}

@Composable
fun StockBottomBar(
    tabs: Array<AppBottomBarTab>,
    currentRoute: Navigator.NavTarget,
    actionNavigate: (Navigator.NavTarget) -> Unit
) {
    val currentTab = tabs.first { it.route == currentRoute }
    val springSpec = SpringSpec<Float>(stiffness = 800f, dampingRatio = 0.8f)

    StockBottomBarLayout(
        selectedIndex = tabs.indexOf(currentTab),
        indicator = { StockBottomNavIndicator() },
        animSpec = springSpec,
        itemCount = tabs.size,
        modifier = Modifier
            .background(StockTheme.colors.surface)
    ) {
        tabs.forEach { tab ->
            val selected = tab == currentTab
            val tint by animateColorAsState(
                if (selected) {
                    StockTheme.colors.primary
                } else {
                    StockTheme.colors.surfaceSecondary
                }
            )

            val text = stringResource(tab.title).uppercase(
                ConfigurationCompat.getLocales(LocalConfiguration.current).get(0)
            )

            StockBottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        tint = tint,
                        contentDescription = text
                    )
                },
                text = {
                    Text(
                        text = text,
                        color = tint,
                        style = StockTheme.typography.button,
                        maxLines = 1
                    )
                },
                selected = selected,
                onSelected = { actionNavigate(tab.route) },
                animSpec = springSpec,
                modifier = BottomNavigationItemPadding
                    .clip(BottomNavIndicatorShape)
            )
        }
    }
}

@Composable
private fun StockBottomBarLayout(
    selectedIndex: Int,
    itemCount: Int,
    animSpec: AnimationSpec<Float>,
    indicator: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Animate the position of the indicator
    val indicatorIndex = remember { Animatable(0f) }
    val targetIndicatorIndex = selectedIndex.toFloat()
    LaunchedEffect(targetIndicatorIndex) {
        indicatorIndex.animateTo(targetIndicatorIndex, animSpec)
    }

    Layout(
        modifier = modifier.height(BottomNavHeight),
        content = {
            content()
            Box(Modifier.layoutId("indicator"), content = indicator)
        }
    ) { measurables, constraints ->

        // Divide the width into n+1 slots and give the selected item 2 slots
        val unselectedWidth = constraints.maxWidth / (itemCount + 1)
        val selectedWidth = 2 * unselectedWidth
        val indicatorMeasurable = measurables.first { it.layoutId == "indicator" }

        val indicatorPlaceable = indicatorMeasurable.measure(
            constraints.copy(minWidth = selectedWidth, maxWidth = selectedWidth)
        )

        val itemPlaceables = measurables
            .filterNot { it == indicatorMeasurable }
            .mapIndexed { index, measurable ->
                // Animate item's width based upon the selection amount
                val width = if (selectedIndex == index) selectedWidth else unselectedWidth
                measurable.measure(
                    constraints.copy(minWidth = width, maxWidth = width)
                )
            }

        layout(
            width = constraints.maxWidth,
            height = itemPlaceables.maxByOrNull { it.height }?.height ?: 0
        ) {
            val indicatorLeft = indicatorIndex.value * unselectedWidth
            indicatorPlaceable.placeRelative(x = indicatorLeft.toInt(), y = 0)
            var x = 0
            itemPlaceables.forEach { placeable ->
                placeable.placeRelative(x = x, y = 0)
                x += placeable.width
            }
        }
    }
}

@Composable
private fun StockBottomNavigationItem(
    icon: @Composable (BoxScope.() -> Unit),
    text: @Composable (BoxScope.() -> Unit),
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
    animSpec: SpringSpec<Float>
) {
    Box(
        modifier = modifier.selectable(selected = selected, onClick = onSelected),
        contentAlignment = Alignment.Center
    ) {
        // Animate the icon/text positions within the item based on selection
        val animationProgress by animateFloatAsState(if (selected) 1f else 0f, animSpec)
        StockBottomNavItemLayout(
            icon = icon,
            text = text,
            animationProgress = animationProgress
        )
    }
}

@Composable
private fun StockBottomNavItemLayout(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float
) {
    Layout(
        content = {
            Box(
                modifier = Modifier
                    .layoutId("icon")
                    .padding(horizontal = TextIconSpacing),
                content = icon
            )
            val scale = lerp(0.6f, 1f, animationProgress)
            Box(
                modifier = Modifier
                    .layoutId("text")
                    .padding(horizontal = TextIconSpacing)
                    .graphicsLayer {
                        alpha = animationProgress
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = TransformOrigin(0f, 0.5f)
                    },
                content = text
            )
        }
    ) { measurables, constraints ->
        val iconPlaceable = measurables.first { it.layoutId == "icon" }.measure(constraints)
        val textPlaceable = measurables.first { it.layoutId == "text" }.measure(constraints)

        placeTextAndIcon(
            textPlaceable,
            iconPlaceable,
            constraints.maxWidth,
            constraints.maxHeight,
            animationProgress
        )
    }
}

private fun MeasureScope.placeTextAndIcon(
    textPlaceable: Placeable,
    iconPlaceable: Placeable,
    width: Int,
    height: Int,
    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float
): MeasureResult {
    val iconY = (height - iconPlaceable.height) / 2
    val textY = (height - textPlaceable.height) / 2

    val textWidth = textPlaceable.width * animationProgress
    val iconX = (width - textWidth - iconPlaceable.width) / 2
    val textX = iconX + iconPlaceable.width

    return layout(width, height) {
        iconPlaceable.placeRelative(iconX.toInt(), iconY)
        if (animationProgress != 0f) {
            textPlaceable.placeRelative(textX.toInt(), textY)
        }
    }
}

@Composable
private fun StockBottomNavIndicator(
    color: Color = StockTheme.colors.surfaceSecondary,
    shape: Shape = BottomNavIndicatorShape
) {
    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .then(BottomNavigationItemPadding)
            .clip(shape)
            .border(2.dp, color, shape)
    )
}

data class AppBottomBarTab(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: Navigator.NavTarget
)

private val BottomNavHeight = 50.dp
private val TextIconSpacing = 2.dp
private val BottomNavIndicatorShape = RoundedCornerShape(4.dp)
private val BottomNavigationItemPadding = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)