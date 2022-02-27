package ru.er_log.stock.android.features.exchange

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.er_log.stock.android.R
import ru.er_log.stock.android.databinding.ActivityExchangeBinding
import ru.er_log.stock.android.databinding.DialogLotCreationBinding
import java.math.BigDecimal

class ExchangeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityExchangeBinding
    private val viewModel: ExchangeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarAccount.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_account)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_active_lots, R.id.nav_completed_deals
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        updateNavHeaderInfo(navView)

        binding.appBarAccount.fab.setOnClickListener {
            createLotCreationDialog(binding.root)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.feedback.collect { if (it.isNotEmpty()) showToast(it) }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.errors.collect(::handleError)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.exchange, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_account)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun updateNavHeaderInfo(navView: NavigationView) {
        val headerView = navView.getHeaderView(0)
        val headerUsername: TextView = headerView.findViewById(R.id.username)
        val headerEmail: TextView = headerView.findViewById(R.id.userEmail)
        TODO()
//        PreferencesStorage.fetchUserProfile()?.let {
//            headerUsername.text = it.userName
//            headerEmail.text = it.userEmail
//        }
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
        Log.e(this::class.simpleName, error.stackTraceToString())
    }

    private fun createLotCreationDialog(parentView: ViewGroup) {
        val dialog = BottomSheetDialog(this)
        val binding = DialogLotCreationBinding.inflate(layoutInflater, parentView, false)

        binding.buttonSubmitLotCreation.setOnClickListener {
            val price = validatePrice(binding.price.text.toString())
            if (price == null) {
                showToast(R.string.toast_check_filed)
            } else {
                val isPurchase = binding.switchSalePurchase.isChecked
                viewModel.createLot(price, isPurchase)
                dialog.dismiss()
            }
        }

        dialog.setTitle(R.string.lot_creation_title)
        dialog.setContentView(binding.root)
        dialog.show()
    }

    private fun validatePrice(priceStr: String): BigDecimal? {
        return try {
            val price = BigDecimal(priceStr)
            if (price <= BigDecimal.ONE) null else price
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(@StringRes text: Int) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}