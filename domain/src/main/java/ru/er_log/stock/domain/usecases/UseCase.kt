package ru.er_log.stock.domain.usecases

import kotlinx.coroutines.*

interface UseCase<in Params, out Value> {
    operator fun invoke(
        params: Params,
        scope: CoroutineScope = GlobalScope,
        onResult: suspend (Result<Value>) -> Unit = {}
    )
}

/**
 * Use case that return single value only once.
 */
abstract class UseCaseValue<in Params, out Value>(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : UseCase<Params, Value> {

    protected abstract suspend fun run(params: Params): Result<Value>

    override operator fun invoke(
        params: Params,
        scope: CoroutineScope,
        onResult: suspend (Result<Value>) -> Unit
    ) {
        scope.launch {
            val result = withContext(defaultDispatcher) {
                try {
                    run(params)
                } catch (t: Throwable) {
                    Result.failure(t)
                }}
            onResult(result)
        }
    }
}

/**
 * Use case that apply action for each item in flow.
 */
abstract class UseCaseFlow<in Params, out Value>(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : UseCase<Params, Value> {

    protected abstract suspend fun run(
        params: Params,
        onEach: suspend (Result<Value>) -> Unit
    )

    override operator fun invoke(
        params: Params,
        scope: CoroutineScope,
        onResult: suspend (Result<Value>) -> Unit
    ) {
        scope.launch {
            withContext(defaultDispatcher) {
                run(params) {
                    scope.launch { onResult(it) }
                }
            }
        }
    }
}