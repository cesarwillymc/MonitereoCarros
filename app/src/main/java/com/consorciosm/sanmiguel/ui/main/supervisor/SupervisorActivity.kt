package com.consorciosm.sanmiguel.ui.main.supervisor

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
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
//        val headerView = navview.getHeaderView(0)
//        setInfoUser(headerView)
        navController = findNavController(R.id.nav_host_supervisor)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_registro, R.id.nav_registro_vehiculo, R.id.nav_partes
        ) , drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navview.setupWithNavController(navController)

    }


    private fun setInfoUser(headerView: View?) {

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }

    override fun getLayout(): Int = R.layout.activity_supervisor
}
