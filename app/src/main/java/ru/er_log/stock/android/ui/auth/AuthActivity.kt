package ru.er_log.stock.android.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ru.er_log.stock.android.R
import ru.er_log.stock.android.PreferencesStorage
import ru.er_log.stock.android.databinding.ActivityLoginBinding
import ru.er_log.stock.android.ui.account.AccountActivity
import ru.er_log.stock.domain.models.LoggedInUser

class AuthActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        authViewModel.loginFormState.observe(this@AuthActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        authViewModel.loginResult.observe(this@AuthActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.failure != null) {
                showLoginFailed(R.string.login_failed, loginResult.failure)
            } else if (loginResult.success != null) {
                processAuthData(loginResult.success)
                redirectUserToAccount(loginResult.success)
            }
        })

        username.afterTextChanged {
            authViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                authViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        authViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                authViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun processAuthData(success: LoggedInUser) {
        PreferencesStorage.saveAuthToken(success.token)
    }

    private fun redirectUserToAccount(model: LoggedInUser) {
        Toast.makeText(
            applicationContext,
            getString(R.string.welcome, model.userName),
            Toast.LENGTH_LONG
        ).show()

        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun showLoginFailed(@StringRes errorString: Int, message: String) {
        val error = getString(errorString) + "\n" + message
        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}