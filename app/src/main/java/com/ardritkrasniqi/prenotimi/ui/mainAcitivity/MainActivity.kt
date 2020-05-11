package com.ardritkrasniqi.prenotimi.ui.mainAcitivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.ActivityMainBinding
import com.ardritkrasniqi.prenotimi.preferences.PreferenceProvider
import com.ardritkrasniqi.prenotimi.ui.dayUI.DayFragment
import com.ardritkrasniqi.prenotimi.ui.mainPage.MainFragment
import com.ardritkrasniqi.prenotimi.ui.shtoRezervimDialog.ShtoRezervimDialog
import com.ardritkrasniqi.prenotimi.utils.DrawerLocker
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL



class MainActivity : AppCompatActivity(), ShtoRezervimDialog.DialogClosedListener,
    NavigationView.OnNavigationItemSelectedListener
    , DrawerLocker {

    private var isDayFragment = true


    // listens for the attached fragment, in this case i want to call a fun if shtorezdialog is closed
    override fun onAttachFragment(fragment: androidx.fragment.app.Fragment) {
        if (fragment is ShtoRezervimDialog) {
            fragment.setDialogClosedListener(this)
        } else if(fragment is DayFragment){
            isDayFragment = true
        }
    }


    // overrides the fun from shtorezervimindialog to call a function in another fragment
    override fun dialogIsClosed() {
            val navHostFragment =
                supportFragmentManager.primaryNavigationFragment as NavHostFragment
        if(!isDayFragment) {
            val mainFragment =
                navHostFragment.childFragmentManager.primaryNavigationFragment as MainFragment
            mainFragment.getAppointments()
        } else if(isDayFragment){
            val mainFragment = navHostFragment.childFragmentManager.fragments[0] as MainFragment
            mainFragment.getAppointments()
        }

    }


    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var sharedPref: PreferenceProvider
    private lateinit var mainViewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        // checks if has internet
        if(!isNetworkAvailable()){
            Navigation.findNavController(this,R.id.myNavHostFragment).navigate(R.id.action_mainFragment_to_noInternetFragment)
        }

        setSupportActionBar(toolbar)
        drawer = binding.drawerLayout


        //sets up actionBar with Nav controller
        NavigationUI.setupActionBarWithNavController(
            this,
            findNavController(R.id.myNavHostFragment),
            drawer
        )
        NavigationUI.setupWithNavController(
            toolbar,
            findNavController(R.id.myNavHostFragment),
            drawer
        )
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // preff and token related stuff :D
        sharedPref = PreferenceProvider(this)
        mainViewModel.token.value = "Bearer ${sharedPref.getToken()}"



        // BOTTOMSHEET DIALOG BEHAVIORS ON ADD appointment button CLICK
        binding.buttonAddEvent.setOnClickListener {
            val dialog =
                ShtoRezervimDialog()
            dialog.show(supportFragmentManager, "BOTTOMSHEETDIALOG")


        }


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment?


        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)

        drawer.addDrawerListener(toggle)
        toggle.syncState()


        // checks if the current navgiation is on authFragment(if it is toolbar is invisible)
        navHostFragment?.navController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.authFragment || destination.id == R.id.noInternetFragment) {
                binding.toolbar.visibility = View.GONE
                binding.buttonAddEvent.visibility = View.GONE
            } else {
                binding.toolbar.visibility = View.VISIBLE
                binding.buttonAddEvent.visibility = View.VISIBLE
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOut -> {
                Log.i("Clicked", "im clicked bruda!")
                sharedPref.saveToken("")
                Navigation.findNavController(this, R.id.myNavHostFragment)
                    .navigate(R.id.action_mainFragment_to_authFragment)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    override fun setDrawerLocked(b: Boolean) {
        if (b) drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        else drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }




    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }


}
