package ru.er_log.stock.android.features.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.er_log.stock.domain.models.`in`.Lot
import ru.er_log.stock.domain.models.`in`.Transaction
import ru.er_log.stock.domain.models.`in`.UserCard
import ru.er_log.stock.domain.usecases.AccountUseCases
import ru.er_log.stock.domain.usecases.ExchangeUseCases

class ProfileViewModel(
    private val accountUseCases: AccountUseCases,
    private val exchangeUseCases: ExchangeUseCases
) : ViewModel() {

    private val _transactions: MutableStateFlow<List<Transaction>> = MutableStateFlow(listOf())
    val transactions = _transactions.apply {
        val page = 0
        accountUseCases.getOperations(page, viewModelScope) {
            it.onSuccess { v -> emit(v) }
        }
    }.asStateFlow()

    private val _userCard = MutableStateFlow<UserCard?>(null)
    val userCard: StateFlow<UserCard?> = _userCard.apply {
        accountUseCases.getUserCard(Unit, viewModelScope) {
            it.onSuccess { v -> emit(v) }
        }
    }.asStateFlow()

    fun onCreateOrder(order: Lot) {
        exchangeUseCases.createOrder(order)
    }

    fun onCreateOffer(offer: Lot) {
        exchangeUseCases.createOffer(offer)
    }
}