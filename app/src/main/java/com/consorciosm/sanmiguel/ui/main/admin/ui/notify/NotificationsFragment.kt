package com.consorciosm.sanmiguel.ui.main.admin.ui.notify

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.constans.Constants.PREF_TOKEN
import com.consorciosm.sanmiguel.common.shared.SharedPreferencsManager.Companion.getSomeStringValue
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.NotificacionesList
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.fragment_notificaciones.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*

class NotificationsFragment : BaseFragment(),KodeinAware ,NotificacionesListener {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    var paginaActual=0
    lateinit var notificacionAdapter: NotificacionAdapter
    override fun getLayout(): Int = R.layout.fragment_notifications

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        notificacionAdapter = NotificacionAdapter(this)
        rv_fragment_ordenes.apply {
            //, LinearLayoutManager.VERTICAL,false
            layoutManager = LinearLayoutManager(view.context)
            setHasFixedSize(true)
            isNestedScrollingEnabled=true
            adapter = notificacionAdapter
        }
        LoadList()
        Search.setOnClickListener {
            paginaActual++
            LoadList()
        }
    }
    fun LoadList(){
        Log.e("token",getSomeStringValue(PREF_TOKEN))
        viewModel.getListNotificacionesSupervisorSMS(paginaActual).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    progressBar.visibility= View.VISIBLE
                }
                is Resource.Success->{
                    Log.e("error",it.data.toString())
                    searchFecha.setText("Pagina ${paginaActual+1}")
                    notificacionAdapter.updateData(it.data)
                    progressBar.visibility= View.GONE
                }
                is Resource.Failure->{
                    snakBar(" ${it.exception.message}")
                    Log.e("error", it.exception.message)
                    progressBar.visibility= View.GONE
                }
            }
        })
    }


    override fun listener(notificacionesList: NotificacionesList, position: Int) {
        if (notificacionesList.isOrder){
            val noti= NotificationsFragmentDirections.actionNavNotifysToOrdenes (false,notificacionesList._id,false)
            findNavController().navigate(noti)
        }else{
            snakBar("No tiene mas datos")
        }
    }
}
