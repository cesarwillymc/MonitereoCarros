package com.consorciosm.sanmiguel.ui.main.admin.ui.partes

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.PartesList
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.fragment_partes.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*

class PartesFragment : BaseFragment(),KodeinAware,PartesListener, TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener  {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    lateinit var partesAdapter: PartesAdapter
    override fun getLayout(): Int =R.layout.fragment_partes
    var hora=""
    var minuto=""

    var anio= Calendar.getInstance().get(Calendar.YEAR)
    var mes= Calendar.getInstance().get(Calendar.MONTH)
    var dia= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        partesAdapter = PartesAdapter(this)
        rv_fragment_partes.apply {
            //, LinearLayoutManager.VERTICAL,false
            layoutManager = LinearLayoutManager(view.context)
            setHasFixedSize(true)
            isNestedScrollingEnabled=true
            adapter = partesAdapter
        }
        Search.setOnClickListener {
            LoadList()
        }
        LoadList()
        searchFecha.setOnClickListener {
            showDialogPicker()
        }
        searchHour.setOnClickListener {
            showDialog()
        }

    }
    fun LoadList(){
        val horas=""
        if (hora.isNotEmpty()){
            "$hora:$minuto:00"
        }
        viewModel.getListPartes(horas,"$anio-$mes-$dia").observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    rl_frag_partes.visibility= View.VISIBLE
                }
                is Resource.Success->{
                   partesAdapter.updateData(it.data)
                    rl_frag_partes.visibility= View.GONE
                }
                is Resource.Failure->{
                    snakBar(" ${it.exception.message}")
                    Log.e("error", it.exception.message)
                    rl_frag_partes.visibility= View.GONE
                }
            }
        })
    }
    override fun listener(partesList: PartesList, position: Int) {

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        hora=hourOfDay.toString()
        minuto=minute.toString()
        searchHour.setText("$hora:$minuto:00")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        anio=year
        mes=month
        dia=dayOfMonth
        searchFecha.setText("$anio-$mes-$dia")
    }
    private fun showDialog() {
        TimePickerDialog( requireActivity(),
            this,0,0,
            DateFormat.is24HourFormat(requireActivity())).show()
    }
    private fun showDialogPicker() {
        val datepicker= DatePickerDialog(requireContext(),this,
            anio,
            mes,
            dia)
        datepicker.show()
    }
}
