package com.ardritkrasniqi.prenotimi.ui.mainAcitivity

import android.net.sip.SipSession
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ardritkrasniqi.prenotimi.R
import com.ardritkrasniqi.prenotimi.databinding.ActivityMainBinding
import com.ardritkrasniqi.prenotimi.ui.shtoRezervimDialog.ShtoRezervimDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(toolbar)

        val mainViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)


//        val navController = this.findNavController(R.id.myNavHostFragment)
//        NavigationUI.setupActionBarWithNavController(this, navController)


        // BOTTOMSHEET DIALOG BEHAVIORS ON ADD CLICK
        binding.buttonAddEvent.setOnClickListener {
            val dialog =
                ShtoRezervimDialog()
            dialog.show(supportFragmentManager, "BOTTOMSHEETDIALOG")
        }







        drawer = findViewById(R.id.drawerLayout)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment?
        supportActionBar?.setHomeButtonEnabled(true)


        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)

        drawer.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



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
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)

    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.myNavHostFragment)
//        return navController.navigateUp()
//    }


}
