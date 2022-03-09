package ru.er_log.stock.android.base.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.math.BigDecimal
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