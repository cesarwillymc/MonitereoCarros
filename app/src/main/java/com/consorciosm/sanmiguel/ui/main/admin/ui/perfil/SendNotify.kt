package com.consorciosm.sanmiguel.ui.main.admin.ui.perfil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.constans.Constants.PREF_ID_USER
import com.consorciosm.sanmiguel.common.shared.SharedPreferencsManager.Companion.getSomeStringValue
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.Notificacion
import com.consorciosm.sanmiguel.data.model.UsuarioList
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.fragment_send_notify.*

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class SendNotify : BaseFragment(), KodeinAware {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    var idConductor =""
    var listaUsuarios = mutableListOf<UsuarioList>()
    var  datos: ArrayAdapter<String>?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        cargarSpinner()
        spinnerDestinatario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                try{
                    idConductor=listaUsuarios[position]._id
                }catch (e:Exception){

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        sendNotify.setOnClickListener {
            if (comprobarDatos()){
                sendValue()
            }
        }
    }

    private fun sendValue() {
        val asunto= AsuntoLbl.text.toString().trim()
        val message= MensajeLbl.text.toString().trim()
        val nombres= spinnerDestinatario.selectedItem.toString()
        viewModel.createNotificacion(Notificacion(idConductor,nombres,getSomeStringValue(PREF_ID_USER)!!,message,asunto)).observe(viewLifecycleOwner,
            Observer {
                when (it) {
                    is Resource.Loading -> {
                        login_progressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        login_progressbar.visibility = View.GONE
                        snakBar("Mensaje enviado correctamente")
                    }
                    is Resource.Failure -> {
                        Log.e("error",it.exception.message!!)
                        login_progressbar.visibility = View.GONE
                        snakBar(it.exception.message!!)
                    }
                }
            })
    }

    private fun comprobarDatos(): Boolean {
        val asunto= AsuntoLbl.text.toString().trim()
        val message= MensajeLbl.text.toString().trim()
        if(asunto.isEmpty()){
            AsuntoLbl.error="Esta vacio"
            AsuntoLbl.requestFocus()
            return false
        }
        if(message.isEmpty()){
            MensajeLbl.error="Esta vacio"
            MensajeLbl.requestFocus()
            return false
        }
        return true
    }

    private fun cargarSpinner() {
        viewModel.getListUser(null).observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    snakBar("Cargando COnductores")
                    login_progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    try {
                        val varible = mutableListOf<String>()
                        listaUsuarios.addAll(it.data)

                        for (dato in it.data) {
                            varible.add("${dato.nombres?:"null"} ${dato.apellidos?:"null"} ")
                        }
                        datos = ArrayAdapter(
                            requireContext(), // Context
                            android.R.layout.simple_spinner_item, // Layout
                            varible// Array
                        )
                        datos!!.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                        spinnerDestinatario.adapter = datos
                        login_progressbar.visibility = View.GONE
                    }catch (e:Exception){
                        Log.e("error","${e.message}")
                    }
                }
                is Resource.Failure -> {
                    Log.e("error",it.exception.message!!)
                    login_progressbar.visibility = View.GONE
                    snakBar(it.exception.message!!)
                }
            }
        })
    }
    override fun getLayout(): Int = R.layout.fragment_send_notify
}
