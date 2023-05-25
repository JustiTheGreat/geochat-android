package com.geochat.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.AppBarConfiguration.Builder
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.geochat.R
import com.geochat.databinding.ActivityMainBinding
import com.geochat.preference_managers.PreferenceManager
import com.geochat.ui.fragments.Chat
import com.geochat.ui.fragments.Chats
import com.geochat.ui.fragments.UtilityFragment

class MainActivity : AppCompatActivity() {
    private val PERMISSION_CODE = 1
    private var appBarConfiguration: AppBarConfiguration? = null
    private var menu: Menu? = null
    private var navigationController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_CODE
            )
        }

        setSupportActionBar(binding.toolbar)
        navigationController = findNavController(this, R.id.navHostFragment)
        appBarConfiguration = Builder(navigationController!!.graph).build()
        setupActionBarWithNavController(this, navigationController!!, appBarConfiguration!!)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_CODE){
            grantResults.forEach { grantResult ->
                run {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        finish()
                        System.exit(0)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        return true
    }

    fun setLogoutVisibility(visible: Boolean) {
        menu?.setGroupVisible(0, visible)
    }

    fun getCurrentFragment(): UtilityFragment {
        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment?)!!
        return navHostFragment.childFragmentManager.fragments[0] as UtilityFragment
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.m_log_out) {
            if (PreferenceManager.authTokenIsAvailable(this)) {
                setLogoutVisibility(false)
                PreferenceManager.removeAuthToken(this)
                val currentFragment: UtilityFragment? = getCurrentFragment()
                if (currentFragment is Chats) {
                    currentFragment.navigateTo(R.id.action_chats_to_login)
                } else if (currentFragment is Chat) {
                    currentFragment.navigateTo(R.id.action_chat_to_login)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return (navigateUp(navigationController!!, appBarConfiguration!!)
                || super.onSupportNavigateUp())
    }
}