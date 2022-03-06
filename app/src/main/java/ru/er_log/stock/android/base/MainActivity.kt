package ru.er_log.stock.android.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.er_log.stock.android.AppGlobalNavigationDirections
import ru.er_log.stock.android.R
import ru.er_log.stock.android.features.auth.ProfileViewModel

class MainActivity : AppCompatActivity() {

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observerAuthenticationState()
    }

    private fun observerAuthenticationState() {
        profileViewModel.profile.observe(this) {
            if (it == null) {
                findNavController().navigate(AppGlobalNavigationDirections.actionToNavAuthLogin())
            }
        }
    }

    /**
     * See https://issuetracker.google.com/142847973
     */
    private fun findNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }
}