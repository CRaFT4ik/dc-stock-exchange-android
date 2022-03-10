package ru.er_log.stock.android.base.utils

import android.content.Context
import android.util.TypedValue
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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

    val decimalFormat = DecimalFormat("#.##")
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

fun TextUnit.spToPx(context: Context): Float {
    if (!this.isSp) throw IllegalStateException()
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this.value, context.resources.displayMetrics
    )
}