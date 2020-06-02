package com.consorciosm.sanmiguel.ui.main.admin.ui.ordenes

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.ConductoresSinOrdenes
import com.consorciosm.sanmiguel.data.model.NotificacionesList
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.fragment_notificaciones.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein
import java.util.*

class NotificacionesFragment : BaseFragment(), KodeinAware,NotificacionesListener {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    lateinit var notificacionAdapter: NotificacionAdapter
    override fun getLayout(): Int =R.layout.fragment_notificaciones
    var paginaActual=0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        notificacionAdapter =
            NotificacionAdapter(
                this
            )
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
        fn_add_order.setOnClickListener {
            val nav=NotificacionesFragmentDirections.actionNavOrdenesToOrdenes(true,"")
            findNavController().navigate(nav)
        }
    }
    fun LoadList(){
        viewModel.getListNotificaciones(paginaActual).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Resource.Loading->{
                    progressBar.visibility= View.VISIBLE
                }
                is Resource.Success->{
                    searchFecha.setText("Pagina ${paginaActual+1}")
                    fn_add_order.visibility= View.VISIBLE
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
        val nav=NotificacionesFragmentDirections.actionNavOrdenesToOrdenes(false,notificacionesList._id,notificacionesList.approved)
        findNavController().navigate(nav)
    }
}



