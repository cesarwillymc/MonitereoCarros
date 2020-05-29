package com.consorciosm.sanmiguel.ui.main.admin.ui.registro_personal

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.PersonalData
import com.consorciosm.sanmiguel.data.model.UsuarioList
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.fragment_registro_personal.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class RegistroPersonalFragment : BaseFragment(),KodeinAware {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    override fun getLayout(): Int =R.layout.fragment_registro_personal
    var new=false
    var id=""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        new=RegistroPersonalFragmentArgs.fromBundle(requireArguments()).new
        id= RegistroPersonalFragmentArgs.fromBundle(requireArguments()).id
        if (!new){
            viewModel.getInfoUserById(id).observe(viewLifecycleOwner,
                Observer {
                    when(it){
                        is Resource.Loading->{
                            login_progressbar.visibility= View.VISIBLE
                        }
                        is Resource.Success->{

                            snakBar("Datos guardados correctamente")
                            login_progressbar.visibility= View.GONE
                            setData(it.data)
                        }
                        is Resource.Failure->{
                            login_progressbar.visibility= View.GONE
                            snakBar(it.exception.message!!)
                        }
                    }
                })
        }
        btn_registrar_frp.setOnClickListener {
            if (comprobarDatos()){
                if (new){
                    crearPersonal()
                }else{
                    updateValues()
                }

            }else{
                snakBar("Datos ingresados no validos.")
            }
        }
    }

    private fun updateValues() {
        val apellidos= et_apellidos_frp.text.toString().trim()
        val dni= et_dni_frp.text.toString().trim()
        val nombre= et_nombre_frp.text.toString().trim()
        val direccion= et_direccion_frp.text.toString().trim()
        val number0= et_telefono_frp.text.toString().trim()
        val number1= et_firstnum_frp.text.toString().trim()
        val number2= et_secondnum_frp.text.toString().trim()
        viewModel.updatePersonal(PersonalData(dni,nombre,direccion,apellidos,number0,number1,number2),id).observe(viewLifecycleOwner,
            Observer {
                when(it){
                    is Resource.Loading->{
                        hideKeyboard()
                        login_progressbar.visibility= View.VISIBLE
                    }
                    is Resource.Success->{
                        snakBar("Datos actualizados correctamente")
                        login_progressbar.visibility= View.GONE
                    }
                    is Resource.Failure->{
                        login_progressbar.visibility= View.GONE
                        snakBar(it.exception.message!!)
                    }
                }
            })
    }

    private fun setData(data: PersonalData) {
        btn_registrar_frp.text="Actualizar datos"
        textView9.visibility=View.VISIBLE
        et_pass_frp.visibility=View.VISIBLE
        et_pass_frp.setText(data.password)
        et_apellidos_frp.setText(data.apellidos)
        et_dni_frp.setText(data.dni)
        et_nombre_frp.setText(data.nombres)
        et_direccion_frp.setText(data.direccion)
        et_telefono_frp.setText(data.telefono)
        et_firstnum_frp.setText(data.telefonoReferenciaA)
        et_secondnum_frp.setText(data.telefonoReferenciaB)
    }

    private fun crearPersonal() {
        val apellidos= et_apellidos_frp.text.toString().trim()
        val dni= et_dni_frp.text.toString().trim()
        val nombre= et_nombre_frp.text.toString().trim()
        val direccion= et_direccion_frp.text.toString().trim()
        val number0= et_telefono_frp.text.toString().trim()
        val number1= et_firstnum_frp.text.toString().trim()
        val number2= et_secondnum_frp.text.toString().trim()
        viewModel.createPersonal(PersonalData(dni,nombre,direccion,apellidos,number0,number1,number2)).observe(viewLifecycleOwner,
            Observer {
                when(it){
                    is Resource.Loading->{
                        hideKeyboard()
                        login_progressbar.visibility= View.VISIBLE
                    }
                    is Resource.Success->{
                        clearData()
                        snakBar("Datos guardados correctamente")
                        login_progressbar.visibility= View.GONE
                    }
                    is Resource.Failure->{
                        login_progressbar.visibility= View.GONE
                        snakBar(it.exception.message!!)
                    }
                }
            })
    }

    private fun clearData() {
        et_apellidos_frp.setText("")
        et_dni_frp.setText("")
        et_nombre_frp.setText("")
        et_direccion_frp.setText("")
        et_telefono_frp.setText("")
        et_firstnum_frp.setText("")
        et_secondnum_frp.setText("")
    }

    private fun comprobarDatos(): Boolean {
        val apellidos= et_apellidos_frp.text.toString().trim()
        val dni= et_dni_frp.text.toString().trim()
        val nombre= et_nombre_frp.text.toString().trim()
        val direccion= et_direccion_frp.text.toString().trim()
        val number0= et_telefono_frp.text.toString().trim()
        if (dni.isEmpty()){
            et_dni_frp.error="Dni esta vacio!"
            et_dni_frp.requestFocus()
            return false
        }
        if (nombre.isEmpty()){
            et_nombre_frp.error="Nombre esta vacio!"
            et_nombre_frp.requestFocus()
            return false
        }
        if (apellidos.isEmpty()){
            et_apellidos_frp.error="Apellido esta vacio!"
            et_apellidos_frp.requestFocus()
            return false
        }
        if (direccion.isEmpty()){
            et_direccion_frp.error="Direccion esta vacio!"
            et_direccion_frp.requestFocus()
            return false
        }
        if (number0.isEmpty()){
            et_telefono_frp.error="Telefono esta vacio!"
            et_telefono_frp.requestFocus()
            return false
        }

        if (dni.length!=8){
            et_dni_frp.error="El dni debe tener 8 digitos"
            et_dni_frp.requestFocus()
            return false
        }
        return true
    }

}
