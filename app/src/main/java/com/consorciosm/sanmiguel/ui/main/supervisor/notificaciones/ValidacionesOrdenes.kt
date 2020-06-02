package com.consorciosm.sanmiguel.ui.main.supervisor.notificaciones

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.NotificacionesList
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.fragment_validaciones_ordenes.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class ValidacionesOrdenes : BaseFragment(),KodeinAware,ValidacionListener {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    lateinit var notificacionAdapter: ValidacionAdapter
    override fun getLayout(): Int =R.layout.fragment_validaciones_ordenes
    var paginaActual=0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        notificacionAdapter =
            ValidacionAdapter(
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

    }
    fun LoadList(){
        viewModel.getListNotificacionesSupervisor(paginaActual).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Resource.Loading->{
                    progressBar.visibility= View.VISIBLE
                }
                is Resource.Success->{
                    searchFecha.setText("Pagina ${paginaActual+1}")
                    progressBar.visibility= View.GONE
                    Log.e("datos",it.data.toString())
                    notificacionAdapter.updateData(it.data)

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

        val nav=ValidacionesOrdenesDirections.actionNavValidacionToValidacionPreview(notificacionesList._id,notificacionesList.approved)
        findNavController().navigate(nav)
    }

}
