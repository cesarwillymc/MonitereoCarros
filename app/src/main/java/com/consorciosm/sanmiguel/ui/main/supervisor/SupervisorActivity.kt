package com.consorciosm.sanmiguel.ui.main.supervisor

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseActivity
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_supervisor.*
import kotlinx.android.synthetic.main.app_bar_inicio_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SupervisorActivity : BaseActivity(),KodeinAware, AppBarConfiguration.OnNavigateUpListener{
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }



        setSupportActionBar(toolbar)
        val headerView = navview.getHeaderView(0)
        setInfoUser(headerView)
        navController = findNavController(R.id.nav_host_supervisor)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(navController.graph)
        //, R.id.nav_permisos,
        //                R.id.nav_personal,R.id.nav_validacion,R.id.nav_perfil
        setupNavigation(navController) //setup navigation
        setupActionBar(navController, appBarConfiguration)

    }
    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }
    private fun setupNavigation(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)
        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)

        //fragments load from here but how ?
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_registro, R.id.nav_registro_vehiculo, R.id.nav_partes
            ) , drawerLayout
        )
    }
    private fun setInfoUser(headerView: View?) {

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //I need to open the drawer onClick
        when (item!!.itemId) {
            android.R.id.home ->
                drawer_layout.openDrawer(GravityCompat.START)
        }
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        //the code is beautiful enough without comments
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }

    override fun getLayout(): Int = R.layout.activity_supervisor
}
