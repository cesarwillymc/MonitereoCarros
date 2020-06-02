package com.consorciosm.sanmiguel.ui.main.supervisor

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.consorciosm.sanmiguel.base.BaseActivity
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import com.google.android.material.badge.BadgeDrawable
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_supervisor.*
import kotlinx.android.synthetic.main.app_bar_inicio_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import  com.consorciosm.sanmiguel.R

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
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_personal, R.id.nav_validacion,R.id.nav_perfil
        ) )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
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
        val badge = nav_view.getOrCreateBadge(nav_view.menu.findItem(R.id.nav_validacion).itemId )
        badge.isVisible = true
        badge.number=0
        viewModel.obtenerNotificacionesCantidad.observe(this, Observer {
            badge.number = it
        })

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
