package ru.er_log.stock.android.base.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


fun ViewModel.scoped(action: () -> Unit) {
    viewModelScope.launch { action() }
}