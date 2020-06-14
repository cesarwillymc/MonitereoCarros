package com.consorciosm.sanmiguel.ui.main.admin

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.*
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseActivity
import com.consorciosm.sanmiguel.ui.auth.AuthViewModelFactory
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_inicio_main.*
import kotlinx.android.synthetic.main.app_bar_inicio_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class InicioMain : BaseActivity(), KodeinAware, AppBarConfiguration.OnNavigateUpListener {
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
            R.id.nav_registro, R.id.nav_registro_vehiculo, R.id.nav_partes,R.id.nav_ordenes,
            R.id.nav_personal ,R.id.nav_perfil,R.id.nav_notifys
        ) , drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navview.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Dexter.withContext(this).withPermissions(
                listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {

                }

            }).check()
        }
    }

    override fun getLayout(): Int = R.layout.activity_inicio_main


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
