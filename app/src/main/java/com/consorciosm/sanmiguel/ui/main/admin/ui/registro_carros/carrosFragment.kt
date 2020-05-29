package com.consorciosm.sanmiguel.ui.main.admin.ui.registro_carros

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.CarrosList
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.fragment_carros.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class carrosFragment : BaseFragment(),KodeinAware ,CarrosListener{
    var value:Boolean?=null
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    lateinit var carrosAdapter: CarrosAdapter
    override fun getLayout(): Int = R.layout.fragment_carros

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        val colors = arrayOf("all","Sin Conductores","Con Conductores")
        val datos = ArrayAdapter(
            requireContext(), // Context
            android.R.layout.simple_spinner_item, // Layout
            colors // Array
        )
        datos.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = datos
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                value = when (position) {
                    0 -> {
                        null
                    }
                    1 -> {
                        false
                    }
                    else -> true
                }
                getCarroslIs()

            }

            override fun onNothingSelected(parent: AdapterView<*>){
            }
        }
        carrosAdapter = CarrosAdapter(this)
        fcarros_rv.apply {
            //, LinearLayoutManager.VERTICAL,false
            layoutManager = LinearLayoutManager(view.context)
            setHasFixedSize(true)
            isNestedScrollingEnabled=true
            adapter = carrosAdapter
        }
        getCarroslIs()
        swiperefresh.setOnRefreshListener {
            getCarroslIs()
            swiperefresh.isRefreshing=false
        }
        add_carros.setOnClickListener {
            val nav=carrosFragmentDirections.actionNavRegistroVehiculoToRegistroVehiculo(true,"null")
            findNavController().navigate(nav)
        }
    }

    private fun getCarroslIs() {
        viewModel.getListCarros(value).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    progressbar.visibility= View.VISIBLE
                }
                is Resource.Success->{
                    progressbar.visibility= View.GONE
                    carrosAdapter.updateData(it.data)
                }
                is Resource.Failure->{
                    snakBar(it.exception.message!!)
                    progressbar.visibility= View.GONE
                }
            }

        })
    }

    override fun listener(carrosList: CarrosList, position: Int) {
        val nav=carrosFragmentDirections.actionNavRegistroVehiculoToRegistroVehiculo(false,carrosList._id)
        findNavController().navigate(nav)
    }


}
