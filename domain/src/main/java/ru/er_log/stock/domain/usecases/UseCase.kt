package ru.er_log.stock.domain.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class UseCase<in Params, out Value> {

    protected abstract suspend fun run(params: Params): Result<Value>

    operator fun invoke(
        params: Params,
        scope: CoroutineScope = GlobalScope,
        onResult: (Result<Value>) -> Unit = {}
    ) {
        scope.launch {
            val deferred = async { run(params) }
            onResult(deferred.await())
        }
    }
}