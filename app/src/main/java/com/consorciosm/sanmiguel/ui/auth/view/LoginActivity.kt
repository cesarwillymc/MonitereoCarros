package com.consorciosm.sanmiguel.ui.auth.view



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseActivity
import com.consorciosm.sanmiguel.common.constans.Constants.ROLE_ADMINISTRADOR
import com.consorciosm.sanmiguel.common.constans.Constants.ROLE_SUPER
import com.consorciosm.sanmiguel.common.constans.Constants.ROLE_SUPERVISOR
import com.consorciosm.sanmiguel.common.shared.SharedPreferencsManager.Companion.clearAllManagerShared
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.ui.auth.AuthViewModel
import com.consorciosm.sanmiguel.ui.auth.AuthViewModelFactory
import com.consorciosm.sanmiguel.ui.main.admin.InicioMain
import com.consorciosm.sanmiguel.ui.main.supervisor.SupervisorActivity
import com.consorciosm.sanmiguel.ui.main.supervisor.validateAccount.ValidateUser
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : BaseActivity(), KodeinAware {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel:AuthViewModel
    private val factory: AuthViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= run {
            ViewModelProvider(this,factory).get(AuthViewModel::class.java)
        }
        al_btn_signin.setOnClickListener {
            val email= al_edtxt_gmail.text.toString().trim()
            val pass= al_edtxt_pass.text.toString().trim()
            userLogin(email, pass)
        }
        al_txt_register.setOnClickListener {
            navRegister()
        }
        viewModel.getLoggetUser.observe(this, Observer {
            if (it!=null){
                Log.e("datos",it.toString())
                if(it.accountActive){
                    snakBar("Te logeaste correctamente")
                    when(it.role){
                        ROLE_SUPERVISOR-> navMainSupervisor()
                        ROLE_ADMINISTRADOR->navMainAdmin()
                        ROLE_SUPER->navSuperAdmin()
                        else->{
                            snakBar("Contactate con un supervisor")
                            logout()
                        }
                    }
                }else{
                    snakBar("No tienes permisos para seguir, contactate con un supervisor")
                    logout()

                }

            }
        })
    }

    private fun navSuperAdmin() {
        val intent = Intent(this, ValidateUser::class.java)
        startActivity(intent)
        finish()
    }

    private fun logout(){
        viewModel.deleteUser()
        clearAllManagerShared()
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

    private fun userLogin(email:String,pass:String) {
        if (email.isEmpty()){
            al_edtxt_gmail.error="Tu dni esta vacio"
            return
        }
        if (pass.isEmpty()){
            al_edtxt_pass.error="Tu contraseña esta vacio"
            return
        }
        if (!viewModel.IsValidNumberDoc(email)){
            al_edtxt_gmail.error="El dni no es valido"
            return
        }
        viewModel.SignIn(email,pass).observe(this, Observer {
            when(it){
                is Resource.Loading->{
                    hideKeyboard()
                    showProgressBar()
                }
                is Resource.Success->{
                    hideProgressBar()
                }
                is Resource.Failure->{
                    snakBar(" ${it.exception.message}")
                    Log.e("error",it.exception.message)
                    hideProgressBar()
                }
            }
        })
    }
    fun navRegister(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)

    }
    fun showProgressBar(){
        login_progressbar.visibility= View.VISIBLE
        al_btn_signin.visibility= View.GONE
    }
    fun hideProgressBar(){
        login_progressbar.visibility= View.GONE
        al_btn_signin.visibility= View.VISIBLE
    }
    override fun getLayout(): Int =R.layout.activity_login
}
