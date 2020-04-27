package com.ardritkrasniqi.prenotimi.ui.mainAcitivity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.ActivityMainBinding
import com.ardritkrasniqi.prenotimi.preferences.PreferenceProvider
import com.ardritkrasniqi.prenotimi.ui.mainPage.MainFragment
import com.ardritkrasniqi.prenotimi.ui.shtoRezervimDialog.ShtoRezervimDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ShtoRezervimDialog.DialogClosedListener {


    // listens for the attached fragment, in this case i want to call a fun if shtorezdialog is closed
    override fun onAttachFragment(fragment: androidx.fragment.app.Fragment) {
        if (fragment is ShtoRezervimDialog) {
            fragment.setDialogClosedListener(this)
        }
    }

    // overrides the fun from shtorezervimindialog to call a function in another fragment
    override fun dialogIsClosed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val mainFragment = navHostFragment.childFragmentManager.fragments[0] as MainFragment
        mainFragment.getAppointments()
    }


    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val mainViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        setSupportActionBar(toolbar)
        drawer = binding.drawerLayout


        NavigationUI.setupActionBarWithNavController(
            this,
            findNavController(R.id.myNavHostFragment),
            drawerLayout
        )
        NavigationUI.setupWithNavController(
            toolbar,
            findNavController(R.id.myNavHostFragment),
            drawer
        )

        // preff and token related stuff :D
        val sharedPref = PreferenceProvider(this)
        mainViewModel.token.value = "Bearer ${sharedPref.getToken()}"



        // BOTTOMSHEET DIALOG BEHAVIORS ON ADD appointment button CLICK
        binding.buttonAddEvent.setOnClickListener {
            findNavController(R.id.myNavHostFragment).navigate(R.id.shtoRezervimDialog)
        }


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment?


        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)

        drawer.addDrawerListener(toggle)
        toggle.syncState()



        // checks if the current navgiation is on authFragment(if it is toolbar is invisible)
        navHostFragment?.navController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.authFragment) {
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





}
