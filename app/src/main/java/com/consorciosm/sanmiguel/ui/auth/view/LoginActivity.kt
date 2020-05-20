package com.consorciosm.sanmiguel.ui.auth.view



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseActivity
import com.consorciosm.sanmiguel.ui.auth.AuthViewModel
import com.consorciosm.sanmiguel.ui.auth.AuthViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class LoginActivity : BaseActivity() {

    private lateinit var viewModel:AuthViewModel
    private val factory: AuthViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= run {
            ViewModelProvider(this,factory).get(AuthViewModel::class.java)
        }
    }

    override fun getLayout(): Int =R.layout.activity_login
}
