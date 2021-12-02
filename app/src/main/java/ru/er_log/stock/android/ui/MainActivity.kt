package ru.er_log.stock.android.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.er_log.stock.android.ui.auth.AuthActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: use sessions
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }
}