package ru.er_log.stock.android.features.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import ru.er_log.stock.domain.usecases.AuthUseCases
import ru.er_log.stock.domain.usecases.ExchangeUseCases

class ProfileViewModel(
    private val authUseCases: AuthUseCases,
    private val exchangeUseCases: ExchangeUseCases
) : ViewModel() {

    private val _transactions: MutableStateFlow<List<Transaction>> = MutableStateFlow(listOf())
    val transactions = _transactions.asStateFlow()

    private val _errors: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errors = _errors.asSharedFlow()

    private val _userCard = MutableStateFlow(UserCard.empty)
    val userCard: StateFlow<UserCard> = _userCard.apply {
        authUseCases.observeUser(Unit, viewModelScope) { result ->
            result.onSuccess {
                it?.let {
                    this.value = UserCard(it, 1231232.toBigDecimal(),
                        UserCard.TransactionStatistics(
                            offersActive = 100,
                            offersCompleted = 1000,
                            ordersActive = 150,
                            ordersCompleted = 2000,
                        )
                    )
                }
            }
//            result.onFailure { this.value = null }
        }
    }

//    fun loadCompletedDeals() {
//        exchangeUseCases.fetchDeals(Unit, viewModelScope) {
//            it.onSuccess { v -> _deals.value = v }
//            it.onFailure { e -> _errors.emit(e) }
//        }
//    }
}