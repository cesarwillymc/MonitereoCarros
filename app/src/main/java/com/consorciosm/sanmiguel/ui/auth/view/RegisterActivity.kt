package com.consorciosm.sanmiguel.ui.auth.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseActivity
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.requestSignUp
import com.consorciosm.sanmiguel.ui.auth.AuthViewModel
import com.consorciosm.sanmiguel.ui.auth.AuthViewModelFactory
import kotlinx.android.synthetic.main.activity_register.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class RegisterActivity : BaseActivity(),KodeinAware{

    override val kodein: Kodein by kodein()
    private lateinit var viewModel:AuthViewModel
    private val factory: AuthViewModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= run {
            ViewModelProvider(this,factory).get(AuthViewModel::class.java)
        }
        register_button_registrar.setOnClickListener {
            userSignUp()
        }

    }
    private fun userSignUp() {

        val name = register_edit_text_nombre.text.toString().trim()
        val paterno = register_edit_text_apellido_p.text.toString().trim()
        val dni = register_edit_text_correo.text.toString().trim()
        val materno = register_edit_text_apellido_m.text.toString().trim()
        val telefono = register_edit_text_cel_ref1.text.toString().trim()
        val direction = register_edit_text_ciudad.text.toString().trim()
        val password = register_edit_text_contraseña.text.toString().trim()

        if (name.isEmpty())
        {
            register_edit_text_nombre.error = "Es necesario ingresar tu nombre."
            register_edit_text_nombre.requestFocus()
            return
        }
        if (paterno.isEmpty())
        {
            register_edit_text_apellido_p.error = "Es necesario ingresar tus Apellido Paterno."
            register_edit_text_apellido_p.requestFocus()
            return
        }
        if (dni.length!=8)
        {
            register_edit_text_correo.error = "Documento de identidad no es valido"
            register_edit_text_correo.requestFocus()
            return
        }
        if (materno.isEmpty())
        {
            register_edit_text_apellido_m.error = "Apellido materno no puede estar vacio"
            register_edit_text_apellido_m.requestFocus()
            return
        }
        if (telefono.isEmpty())
        {
            register_edit_text_cel_ref1.error = "Es necesario tu número de celular"
            register_edit_text_cel_ref1.requestFocus()
        }
        if (direction.isEmpty())
        {
            register_edit_text_ciudad.error = "Ingresa tu direccion"
            register_edit_text_ciudad.requestFocus()
        }

        if (password.isEmpty())
        {
            register_edit_text_contraseña.error = "Ingresa una contraseña."
            register_edit_text_contraseña.requestFocus()
            return
        }
        if (password.length <= 8)
        {
            register_edit_text_contraseña.error = "La contraseña debe de tener al menos 8 caracteres"
            register_edit_text_contraseña.requestFocus()
            return
        }
        val model= requestSignUp(name,"$paterno $materno",dni,telefono,direction,password)
        viewModel.SignUp(model).observe(this, Observer {
            when(it){
                is Resource.Loading->{
                    hideKeyboard()
                    showProgressBar()
                }
                is Resource.Success->{
                    snakBar("Tu registro se completo correctamente!.")
                    hideProgressBar()
                }
                is Resource.Failure->{
                    Log.e("error",it.exception.message)
                    snakBar("Error ${it.exception.message}")
                    hideProgressBar()
                }
            }
        })

    }
    fun showProgressBar(){
        register_progressbar.visibility= View.VISIBLE
        register_button_registrar.visibility= View.GONE
    }
    fun hideProgressBar(){
        register_progressbar.visibility= View.GONE
        register_button_registrar.visibility= View.VISIBLE
    }
    override fun getLayout(): Int = R.layout.activity_register
}
