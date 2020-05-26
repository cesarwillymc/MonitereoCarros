package com.consorciosm.sanmiguel.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseActivity
import com.consorciosm.sanmiguel.common.constans.Constants
import com.consorciosm.sanmiguel.common.constans.Constants.PREF_ID_USER
import com.consorciosm.sanmiguel.common.shared.SharedPreferencsManager
import com.consorciosm.sanmiguel.common.shared.SharedPreferencsManager.Companion.getSomeStringValue
import com.consorciosm.sanmiguel.ui.auth.AuthViewModel
import com.consorciosm.sanmiguel.ui.auth.AuthViewModelFactory
import com.consorciosm.sanmiguel.ui.auth.view.LoginActivity
import com.consorciosm.sanmiguel.ui.main.admin.InicioMain
import com.consorciosm.sanmiguel.ui.main.supervisor.SupervisorActivity
import kotlinx.android.synthetic.main.activity_sphash_screen.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SphashScreen : BaseActivity(),KodeinAware {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel:AuthViewModel
    private val factory: AuthViewModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= run {
            ViewModelProvider(this,factory).get(AuthViewModel::class.java)
        }
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim)
        imgLogoSplash.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                val dato=viewModel.getLoggetUser.value
               if (dato!=null){
                   when(dato.role){
                       Constants.ROLE_SUPERVISOR -> navMainSupervisor()
                       Constants.ROLE_ADMINISTRADOR ->navMainAdmin()
                       else->{
                           snakBar("Contactate con un supervisor")
                           logout()
                       }
                   }
               }else{
                   navigationToPrincipal()
               }
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })
    }
    private fun logout(){
        viewModel.deleteUser()
        SharedPreferencsManager.clearAllManagerShared()
    }
    private fun navigationToPrincipal() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun navMainAdmin() {
        val intent = Intent(this, InicioMain::class.java)
        startActivity(intent)
        finish()
    }

    private fun navMainSupervisor() {
        val intent = Intent(this, SupervisorActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun getLayout(): Int = R.layout.activity_sphash_screen
}