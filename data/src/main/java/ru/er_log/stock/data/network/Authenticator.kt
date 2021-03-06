package ru.er_log.stock.data.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.er_log.stock.data.di.inject
import ru.er_log.stock.domain.repositories.AuthRepository

class Authenticator : Authenticator {

    private val authRepository: AuthRepository by lazy { inject() }

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        // TODO: 1. Check if another request not already updated the token
        //       2. Get updated token or sync. refresh and save token (depends on p.1)
        // return response.request.newBuilder().addToken("").build()
        return null
    }
}