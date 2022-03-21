package ru.er_log.stock.android.features.account

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.utils.toHumanCurrencyFormat
import ru.er_log.stock.android.compose.components.StockBottomSheetScaffold
import ru.er_log.stock.android.compose.components.StockButton
import ru.er_log.stock.android.compose.components.StockCard
import ru.er_log.stock.android.compose.components.StockSurface
import ru.er_log.stock.android.compose.theme.StockColors
import ru.er_log.stock.android.compose.theme.StockTheme
import ru.er_log.stock.android.features.settings.SettingsDialogScreen
import ru.er_log.stock.domain.models.`in`.Lot
import ru.er_log.stock.domain.models.`in`.Transaction
import ru.er_log.stock.domain.models.`in`.UserCard
import ru.er_log.stock.domain.models.`in`.UserInfo
import java.math.BigDecimal
import java.text.SimpleDateFormat
import kotlin.math.absoluteValue
import kotlin.random.Random

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = getViewModel()
) {
    val scope = rememberCoroutineScope()
    val userCardState = profileViewModel.userCard(scope).collectAsState()
    val userOperationsState = profileViewModel.transactions(scope).collectAsState()

    ProfileScreenImpl(
        userCard = { userCardState.value },
        userOperations = { userOperationsState.value },
        onCreateOrder = profileViewModel::onCreateOrder,
        onCreateOffer = profileViewModel::onCreateOffer
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun ProfileScreenImpl(
    userCard: () -> UserCard,
    userOperations: () -> List<Transaction>,
    onCreateOrder: (Lot) -> Unit,
    onCreateOffer: (Lot) -> Unit,
) {
    val screenHeight = with(LocalConfiguration.current) { screenHeightDp }
    val sheetPeekHeight = (screenHeight * 0.2f).dp
    val sheetMaxHeight = (screenHeight * 0.8f).dp
    val scanFoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    StockBottomSheetScaffold(
        scaffoldState = scanFoldState,
        sheetContent = {
            BottomSheetLayer(
                modifier = Modifier
                    .heightIn(max = sheetMaxHeight)
                    .fillMaxHeight(),
                operations = userOperations
            )
        },
        sheetPeekHeight = sheetPeekHeight
    ) { paddings ->
        AccountLayer(
            modifier = Modifier.padding(paddings),
            userCard = userCard,
            onCreateOrder = onCreateOrder,
            onCreateOffer = onCreateOffer
        )
    }
}

@Composable
private fun AccountLayer(
    modifier: Modifier = Modifier,
    userCard: () -> UserCard,
    onCreateOrder: (Lot) -> Unit,
    onCreateOffer: (Lot) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            SettingsIconButton(
                modifier = Modifier
                    .align(TopEnd)
                    .padding(end = 6.dp)
            )

            Crossfade(targetState = userCard()) {
                Column {
                    AccountLayerProfileInfo(it)
                    AccountLayerStatistic(it)
                }
            }
        }

        AccountLayerOperationButtons(onCreateOrder = onCreateOrder, onCreateOffer = onCreateOffer)
    }
}

@Composable
private fun AccountLayerProfileInfo(
    userCard: UserCard
) {
    val profile = userCard.userInfo
    val imageSize = 128.dp

    Box(contentAlignment = TopCenter) {
        Image(
            modifier = Modifier
                .align(TopCenter)
                .size(imageSize)
                .clip(CircleShape)
                .zIndex(2f),
            painter = painterResource(defaultAvatarFor(profile.userName)),
            contentDescription = "Avatar"
        )
        StockSurface(
            modifier = Modifier
                .align(TopCenter)
                .padding(top = imageSize / 2)
                .zIndex(1f)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = imageSize / 2 + 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                CompositionLocalProvider(LocalTextStyle provides StockTheme.typography.body1) {
                    Text(text = profile.userName)
                }

                Spacer(Modifier.size(4.dp))

                CompositionLocalProvider(LocalTextStyle provides StockTheme.typography.body2) {
                    Text(text = profile.userEmail)
                }
            }
        }
    }
}

@Composable
private fun SettingsIconButton(
    modifier: Modifier = Modifier
) {
    val (dialogVisible, shouldShowDialog) = remember { mutableStateOf(false) }

    IconButton(modifier = modifier, onClick = { shouldShowDialog(true) }) {
        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
    }

    if (dialogVisible) {
        SettingsDialogScreen(onDismissRequest = { shouldShowDialog(false) })
    }
}

@Composable
private fun AccountLayerStatistic(
    userCard: UserCard
) {
    CompositionLocalProvider(LocalTextStyle provides StockTheme.typography.subtitle2) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            text = stringResource(R.string.account_profile_statistic_section_title)
        )
    }

    StockSurface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides remember {
                    TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        letterSpacing = 0.6.sp
                    )
                },
            ) {
                Text(userCard.userBalance.toHumanCurrencyFormat())
            }

            Text(
                text = stringResource(R.string.account_profile_statistic_balance_caption),
                fontSize = StockTheme.typography.caption.fontSize,
                color = StockTheme.colors.textSecondary
            )

            Spacer(Modifier.size(16.dp))
            TransactionStatisticBar(userCard.statistics)
        }
    }
}

@Composable
private fun TransactionStatisticBar(
    statistics: UserCard.TransactionStatistics
) {
    val barHeight = 12.dp

    val weights = arrayOf(
        statistics.ordersCompleted, statistics.ordersActive,
        statistics.offersCompleted, statistics.offersActive
    )

    val colors = arrayOf(
        StockColors.ordersColor, StockColors.ordersSecondaryColor,
        StockColors.offersColor, StockColors.offersSecondaryColor
    )

    val descriptions = arrayOf(
        R.string.account_profile_statistic_balance_completed_orders,
        R.string.account_profile_statistic_balance_completed_offers
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(barHeight)
            .clip(RoundedCornerShape(6.dp))
    ) {
        weights.forEachIndexed { i, weight ->
            Box(
                modifier = Modifier
                    .animateContentSize()
                    .fillMaxHeight()
                    .weight(maxOf(weight, 1).toFloat() + 1f, fill = true)
                    .background(color = colors[i])
            )
        }
    }

    Spacer(Modifier.size(6.dp))
    Row(
        verticalAlignment = CenterVertically
    ) {
        descriptions.forEachIndexed { i, text ->
            Spacer(Modifier.size(6.dp))
            Box(
                modifier = Modifier
                    .size(barHeight - 3.dp)
                    .clip(CircleShape)
                    .background(color = colors[i * 2])
            )
            Spacer(Modifier.size(2.dp))
            Box(
                modifier = Modifier
                    .size(barHeight - 3.dp)
                    .clip(CircleShape)
                    .background(color = colors[(i * 2) + 1])
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text = stringResource(text),
                fontSize = StockTheme.typography.caption.fontSize,
                color = StockTheme.colors.textSecondary
            )
            Spacer(Modifier.size(4.dp))
        }
    }
}

@Composable
private fun AccountLayerOperationButtons(
    onCreateOrder: (Lot) -> Unit,
    onCreateOffer: (Lot) -> Unit,
    orderCreationState: LotCreationState = rememberLotCreationDialogState(),
    offerCreationState: LotCreationState = rememberLotCreationDialogState()
) {
    val (dialogVisible, shouldShowDialog) = remember { mutableStateOf(false) }
    val onCreateAction = remember { mutableStateOf<(Lot) -> Unit>({}) }
    val creationState = remember { mutableStateOf(orderCreationState) }

    Row(
        modifier = Modifier.padding(12.dp)
    ) {
        StockButton(
            modifier = Modifier.weight(100f),
            onClick = {
                creationState.value = orderCreationState
                onCreateAction.value = {
                    shouldShowDialog(false)
                    onCreateOrder(it)
                }
                shouldShowDialog(true)
            }
        ) {
            Text(stringResource(R.string.account_action_create_order))
        }

        Spacer(modifier = Modifier.size(12.dp))

        StockButton(
            modifier = Modifier.weight(100f),
            onClick = {
                creationState.value = offerCreationState
                onCreateAction.value = {
                    shouldShowDialog(false)
                    onCreateOffer(it)
                }
                shouldShowDialog(true)
            }
        ) {
            Text(stringResource(R.string.account_action_make_offer))
        }
    }

    if (dialogVisible) {
        LotCreationDialog(
            state = creationState.value,
            onDismissRequest = { shouldShowDialog(false) },
            onCreateAction = onCreateAction.value
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BottomSheetLayer(
    modifier: Modifier = Modifier,
    operations: () -> List<Transaction>
) {
    Column(
        modifier = modifier
            .border(
                1.dp,
                StockTheme.colors.backgroundSecondary.copy(alpha = 0.3f),
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(top = 12.dp, start = 6.dp, end = 6.dp),
    ) {
        BottomSheetTitle(
            title = stringResource(R.string.account_transactions_list_title)
        )

        val isOperationsEmpty by remember { derivedStateOf { operations().isEmpty() } }
        val operationsSize by remember { derivedStateOf { operations().size } }
        Crossfade(targetState = isOperationsEmpty) { isEmpty ->
            when (isEmpty) {
                true -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        text = stringResource(R.string.account_transactions_list_empty),
                        color = StockTheme.colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    val listState = rememberLazyListState()

                    LazyColumn(state = listState) {
                        operations().forEach { transaction ->
                            item(transaction.uid) {
                                TransactionListItem(
                                    modifier = Modifier.animateItemPlacement(),
                                    transaction = transaction
                                )
                            }
                        }
                    }

                    val visibleIndex = listState.firstVisibleItemIndex
                    LaunchedEffect(key1 = visibleIndex) {
                        if (visibleIndex < 3) {
                            listState.animateScrollToItem(0)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomSheetTitle(title: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Icon(
            modifier = Modifier
                .align(TopCenter)
                .widthIn(max = 80.dp),
            imageVector = Icons.Default.Maximize, contentDescription = "Expand sheet",
            tint = StockTheme.colors.textSecondary.copy(alpha = 0.7f)
        )

        CompositionLocalProvider(LocalTextStyle provides StockTheme.typography.subtitle1) {
            Text(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 10.dp),
                text = title,
                color = StockTheme.colors.textSecondary
            )
        }
    }
}

@Composable
private fun TransactionListItem(
    modifier: Modifier = Modifier,
    transaction: Transaction
) {
    val total = remember {
        (transaction.lot.amount * transaction.lot.price).toHumanCurrencyFormat()
    }
    val (cardExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expandIconRotateState = animateFloatAsState(
        targetValue = if (cardExpanded) -180f else 0f,
        visibilityThreshold = 1f
    )

    val shape = RoundedCornerShape(6.dp)
    StockCard(
        shape = shape,
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .border(1.dp, StockTheme.colors.background.copy(alpha = 0.3f), shape)
            .clickable { setExpanded(!cardExpanded) }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .heightIn(min = 40.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                val icon = when (transaction.type) {
                    Transaction.Type.OFFER -> Icons.Default.West
                    Transaction.Type.ORDER -> Icons.Default.East
                    else -> Icons.Outlined.HelpOutline
                }
                val tint = when {
                    transaction.isPending -> StockColors.progressColor
                    else -> when (transaction.type) {
                        Transaction.Type.OFFER -> StockColors.offersColor
                        Transaction.Type.ORDER -> StockColors.ordersColor
                        else -> Color.Gray
                    }
                }.copy(alpha = 0.65f)

                Icon(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp),
                    imageVector = icon, tint = tint, contentDescription = "Operation type"
                )

                Row(
                    verticalAlignment = CenterVertically
                ) {
                    val sign = when (transaction.type) {
                        Transaction.Type.ORDER -> "+"
                        Transaction.Type.OFFER -> "-"
                        else -> ""
                    }
                    Text("$sign ${transaction.lot.amount}")

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        modifier = Modifier.rotate(expandIconRotateState.value),
                        imageVector = Icons.Default.ExpandMore,
                        tint = LocalContentColor.current.copy(alpha = 0.4f),
                        contentDescription = "Expand more"
                    )
                }
            }

            AnimatedVisibility(visible = cardExpanded) {
                val time = SimpleDateFormat.getDateTimeInstance().format(transaction.timestamp)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ) {
                    CompositionLocalProvider(LocalContentColor provides StockTheme.colors.textSecondary) {
                        Text(
                            stringResource(
                                R.string.account_transaction_detail_amount,
                                transaction.lot.amount
                            )
                        )
                        Text(
                            stringResource(
                                R.string.account_transaction_detail_price,
                                transaction.lot.price.toHumanCurrencyFormat()
                            )
                        )
                        Text(
                            stringResource(
                                R.string.account_transaction_detail_total,
                                total
                            )
                        )
                        Text(modifier = Modifier.align(End), text = time)
                    }
                }
            }
        }
    }
}

@DrawableRes
private fun defaultAvatarFor(userName: String): Int {
    val avatars = arrayOf(
        R.drawable.ic_avatar_1,
        R.drawable.ic_avatar_2,
        R.drawable.ic_avatar_3,
        R.drawable.ic_avatar_4,
        R.drawable.ic_avatar_5,
        R.drawable.ic_avatar_6,
        R.drawable.ic_avatar_7,
        R.drawable.ic_avatar_8,
        R.drawable.ic_avatar_9
    )
    return avatars[userName.hashCode().absoluteValue % avatars.size]
}

@Preview
@Composable
private fun AccountLayerPreview() {
    val profile = UserInfo("Anna Goldberg", "anna.goldberg@gmail.com")
    val balance = BigDecimal.valueOf(8471639301)
    val statistics = UserCard.TransactionStatistics(
        offersActive = 100,
        offersCompleted = 1000,
        ordersActive = 150,
        ordersCompleted = 2000,
    )
    AccountLayer(
        userCard = { UserCard(profile, balance, statistics) },
        onCreateOrder = {},
        onCreateOffer = {}
    )
}

@Preview
@Composable
private fun OperationsLayerPreview() {
    val operations: List<Transaction> = mutableListOf<Transaction>().apply {
        repeat(5) { add(randomTransaction()) }
    }
    val items = remember { mutableStateOf(operations) }

    Column {
        LaunchedEffect(this::class) {
            while (true) {
                delay(2000)
                val newList = items.value.toMutableList()
                newList.add(0, randomTransaction())
                items.component2().invoke(newList)
            }
        }

        BottomSheetLayer(
            modifier = Modifier.fillMaxHeight()
        ) { items.value }
    }
}

@Preview
@Composable
private fun OperationsLayerEmptyPreview() {
    Column {
        BottomSheetLayer { emptyList() }
    }
}

private fun randomTransaction() = Transaction(
    uid = Random.nextBits(32).toString(),
    lot = Lot(
        price = Random.nextDouble(100.0, 20000.0).toBigDecimal(),
        amount = Random.nextDouble(0.1, 10.0).toBigDecimal(),
    ),
    timestamp = Random.nextLong(),
    type = if (Random.nextBoolean()) Transaction.Type.ORDER else Transaction.Type.OFFER,
    isPending = Random.nextBoolean()
)