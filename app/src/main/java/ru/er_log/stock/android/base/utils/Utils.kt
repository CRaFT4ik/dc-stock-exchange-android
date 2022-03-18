package ru.er_log.stock.android.base.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.node.Ref
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.er_log.stock.android.BuildConfig
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat


fun ViewModel.scoped(action: () -> Unit) {
    viewModelScope.launch { action() }
}

fun ViewModel.scopedJob(action: () -> Unit): Job {
    return viewModelScope.launch { action() }
}

fun BigDecimal.toHumanFormat(): String {
    val arr = arrayOf("", "k", "m", "b", "t", "p", "e")

    var postfix = ""
    var result = this
    val divisor = BigDecimal.valueOf(1000)
    var index = 0
    if (this.abs() > BigDecimal.valueOf(10000)) {
        result.divide(divisor)

        while (true) {
            val k = result.divide(divisor)
            if (k <= BigDecimal.ONE) break
            result = k
            index++
        }
    }

    when {
        index >= arr.size -> result = this
        else -> postfix = arr[index]
    }

    val decimalFormat = DecimalFormat("0.00")
    return decimalFormat.format(result) + postfix
}

fun BigDecimal.autoScale(): BigDecimal {
    val scale = when {
        setScale(0, RoundingMode.DOWN).compareTo(BigDecimal.ZERO) == 0 -> 5
        compareTo(BigDecimal.TEN) < 0 -> 3
        else -> 2
    }
    return setScale(scale, RoundingMode.HALF_EVEN)
}

fun BigDecimal.toHumanCurrencyFormat(): String {
    val decimalFormat = DecimalFormat("###,###,###,##0.00")
    return decimalFormat.format(this).replace(',', ' ')
}

@Composable
inline fun LogCompositions(tag: String) {
    if (BuildConfig.DEBUG) {
        val ref = remember { Ref<Int>().apply { value = 0 } }
        SideEffect { ref.value?.apply { inc() }}
        Log.d(tag, "Compositions: ${ref.value}")
    }
}

fun Boolean?.onlyFalse(): Boolean = this != null && !this

fun Boolean?.falseOrNull(): Boolean = this == null || !this