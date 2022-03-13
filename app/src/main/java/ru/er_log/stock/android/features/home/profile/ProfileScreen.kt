package ru.er_log.stock.android.features.home.profile

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Maximize
import androidx.compose.material.icons.filled.West
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.node.Ref
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
import org.koin.androidx.compose.getViewModel
import ru.er_log.stock.android.BuildConfig
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.utils.toHumanCurrencyFormat
import ru.er_log.stock.android.base.utils.toHumanFormat
import ru.er_log.stock.android.compose.components.AppBottomSheetScaffold
import ru.er_log.stock.android.compose.components.AppButton
import ru.er_log.stock.android.compose.components.AppCard
import ru.er_log.stock.android.compose.components.AppSurface
import ru.er_log.stock.android.compose.theme.AppColors
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.domain.api.v1.exchange.LotCreationRequest
import ru.er_log.stock.domain.models.auth.UserProfile
import ru.er_log.stock.domain.models.exchange.Lot
import java.math.BigDecimal
import java.text.SimpleDateFormat
import kotlin.random.Random

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = getViewModel()
) {
    val screenHeight = with(LocalConfiguration.current) { screenHeightDp }
    val sheetPeekHeight = (screenHeight * 0.3f).dp
    val sheetMaxHeight = (screenHeight * 0.8f).dp
    val scanFoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    val userProfile = profileViewModel.userCard.collectAsState()

    AppBottomSheetScaffold(
        scaffoldState = scanFoldState,
        sheetContent = {
            BottomSheetLayer(
                modifier = Modifier.heightIn(max = sheetMaxHeight),
                operations = profileViewModel.transactions.collectAsState()
            )
        },
        sheetPeekHeight = sheetPeekHeight
    ) { paddings ->
        AccountLayer(
            modifier = Modifier.padding(paddings),
            userCard = { userProfile.value }
        )
    }
}

@Composable
private fun AccountLayer(
    modifier: Modifier = Modifier,
    userCard: () -> UserCard
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AccountLayerProfileInfo(userCard)
        AccountLayerStatistic(userCard)
        AccountLayerOperationButtons()
    }
}

@Composable
private fun AccountLayerProfileInfo(
    userCard: () -> UserCard
) {
    val profile = userCard().userProfile
    val imageSize = 128.dp

    Box(contentAlignment = TopCenter) {
        Image(
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
                .zIndex(2f),
            painter = painterResource(defaultAvatarFor(profile.userName)),
            contentDescription = "Avatar"
        )
        AppSurface(
            modifier = Modifier
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
                CompositionLocalProvider(LocalTextStyle provides AppTheme.typography.body1) {
                    Text(text = profile.userName)
                }

                Spacer(Modifier.size(4.dp))

                CompositionLocalProvider(LocalTextStyle provides AppTheme.typography.body2) {
                    Text(text = profile.userEmail)
                }
            }
        }
    }
}

@Composable
private fun AccountLayerStatistic(
    userCard: () -> UserCard
) {
    CompositionLocalProvider(LocalTextStyle provides AppTheme.typography.subtitle2) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            text = stringResource(R.string.account_profile_statistic_section_title)
        )
    }

    AppSurface {
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
                Text(
                    stringResource(
                        R.string.abstract_currency,
                        userCard().userBalance.toHumanCurrencyFormat()
                    )
                )
            }

            Text(
                text = stringResource(R.string.account_profile_statistic_balance_caption),
                fontSize = AppTheme.typography.caption.fontSize,
                color = AppTheme.colors.textSecondary
            )

            Spacer(Modifier.size(16.dp))
            TransactionStatisticBar(state = { userCard().statistics })
        }
    }
}

@Composable
private fun ColumnScope.TransactionStatisticBar(
    state: () -> UserCard.TransactionStatistics
) {
    val statistics = state.invoke()
    val barHeight = 12.dp

    val weights = arrayOf(
        statistics.ordersCompleted, statistics.ordersActive,
        statistics.ordersCompleted, statistics.offersActive
    )

    val colors = arrayOf(
        AppColors.ordersSecondaryColor, AppColors.ordersColor,
        AppColors.offersSecondaryColor, AppColors.offersColor
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
                    .fillMaxHeight()
                    .weight(maxOf(weight, 1).toFloat(), fill = true)
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
                fontSize = AppTheme.typography.caption.fontSize,
                color = AppTheme.colors.textSecondary
            )
            Spacer(Modifier.size(4.dp))
        }
    }
}

@Composable
private fun ColumnScope.AccountLayerOperationButtons(
    onCreateOrder: (LotCreationRequest) -> Unit = {},
    onCreateOffer: (LotCreationRequest) -> Unit = {},
    orderCreationState: LotCreationState = rememberLotCreationDialogState(),
    offerCreationState: LotCreationState = rememberLotCreationDialogState()
) {
    val (dialogVisible, shouldShowDialog) = remember { mutableStateOf(false) }
    val onCreateAction = remember { mutableStateOf<(LotCreationRequest) -> Unit>({}) }
    val creationState = remember { mutableStateOf(orderCreationState) }

    Row(
        modifier = Modifier.padding(12.dp)
    ) {
        AppButton(
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
            Text("Create order")
        }

        Spacer(modifier = Modifier.size(12.dp))

        AppButton(
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
            Text("Make offer")
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

@Composable
private fun ColumnScope.BottomSheetLayer(
    modifier: Modifier = Modifier,
    operations: State<List<Transaction>>
) {
    Column(
        modifier = modifier.padding(top = 12.dp, start = 6.dp, end = 6.dp)
    ) {
        BottomSheetTitle(
            title = stringResource(R.string.account_transactions_list_title)
        )

        Crossfade(
            targetState = operations.value.isEmpty()
        ) { listIsEmpty ->
            when (listIsEmpty) {
                true -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        text = stringResource(R.string.account_transactions_list_empty),
                        color = AppTheme.colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    LazyColumn {
                        operations.value.forEach {
                            item(it.uid) {
                                TransactionListItem(it)
                            }
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
            tint = AppTheme.colors.textSecondary.copy(alpha = 0.7f)
        )

        CompositionLocalProvider(LocalTextStyle provides AppTheme.typography.subtitle1) {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                text = title,
                color = AppTheme.colors.textSecondary
            )
        }
    }
}

@Composable
private fun TransactionListItem(
    transaction: Transaction
) {
    val total = remember { (transaction.amount * transaction.price).toHumanFormat() }
    val (cardExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expandIconRotateState = animateFloatAsState(
        targetValue = if (cardExpanded) -180f else 0f,
        visibilityThreshold = 1f
    )

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { setExpanded(!cardExpanded) }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                val icon = when (transaction.type) {
                    Transaction.Type.SELL -> Icons.Default.West
                    Transaction.Type.BUY -> Icons.Default.East
                }
                val tint = when {
                    transaction.inProgress -> AppColors.progressColor
                    else -> when (transaction.type) {
                        Transaction.Type.SELL -> AppColors.offersColor
                        Transaction.Type.BUY -> AppColors.ordersColor
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
                    Text(stringResource(R.string.abstract_currency, total))

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
                val time = remember {
                    SimpleDateFormat.getDateTimeInstance().format(transaction.timestamp)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ) {
                    CompositionLocalProvider(LocalContentColor provides AppTheme.colors.textSecondary) {
                        Text("price: " + transaction.price)
                        Text("amount: " + transaction.amount)
                        Text("time: $time")
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
    return avatars[userName.hashCode() % avatars.size]
}

@Preview
@Composable
private fun AccountLayerPreview() {
    val profile = UserProfile("Anna Goldberg", "anna.goldberg@gmail.com")
    val balance = BigDecimal.valueOf(8471639301)
    val statistics = UserCard.TransactionStatistics(
        offersActive = 100,
        offersCompleted = 1000,
        ordersActive = 150,
        ordersCompleted = 2000,
    )
    AccountLayer(
        userCard = { UserCard(profile, balance, statistics) }
    )
}

@Preview
@Composable
private fun OperationsLayerPreview() {
    val operations: List<Transaction> = mutableListOf<Transaction>().apply {
        repeat(20) { add(randomTransaction()) }
    }

    Column {
        BottomSheetLayer(
            operations = remember { mutableStateOf(operations) }
        )
    }
}

@Preview
@Composable
private fun OperationsLayerEmptyPreview() {
    Column {
        BottomSheetLayer(
            operations = remember { mutableStateOf(emptyList()) }
        )
    }
}

private fun randomTransaction() = Transaction(
    uid = Random.nextBits(32).toString(),
    price = Random.nextDouble(100.0, 20000.0).toBigDecimal(),
    amount = Random.nextDouble(0.1, 10.0).toBigDecimal(),
    timestamp = Random.nextLong(),
    type = if (Random.nextBoolean()) Transaction.Type.BUY else Transaction.Type.SELL,
    inProgress = Random.nextBoolean()
)