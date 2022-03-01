package ru.er_log.stock.android.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.er_log.stock.android.R
import ru.er_log.stock.android.features.auth.login.AuthActivity

class MainActivity : AppCompatActivity(R.layout.activity_exchange) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: use sessions
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(intent)
    }
}