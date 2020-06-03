package com.consorciosm.sanmiguel.ui.main.admin.ui.ordenes

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.constans.Constants.BASE_URL_AMAZON_S3
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.ConductoresSinOrdenes
import com.consorciosm.sanmiguel.data.model.OrdenProgramada
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.fragment_programar_salida.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class OrdernesProgramadas : BaseFragment(), KodeinAware , TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    var new = true
    var id = ""
    override fun getLayout(): Int = R.layout.fragment_programar_salida
    var listaUsuarios = mutableListOf<ConductoresSinOrdenes>()
    var  datos: ArrayAdapter<String>?=null
    var ordenProgramada:OrdenProgramada?=null
    var idConductor=""


    //Horas
    var hora=0
    var minuto=0

    var anio= Calendar.getInstance().get(Calendar.YEAR)
    var mes= Calendar.getInstance().get(Calendar.MONTH)
    var dia= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)


    var tipe=""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = requireActivity().run {
            ViewModelProvider(this, factory).get(ViewModelMain::class.java)
        }
        new = OrdernesProgramadasArgs.fromBundle(requireArguments()).new
        id = OrdernesProgramadasArgs.fromBundle(requireArguments()).id
        if (!new) {
            LoadDataNotify()
            if (OrdernesProgramadasArgs.fromBundle(requireArguments()).isValid){
                fps_descargar.visibility= View.VISIBLE
            }
        }else{
            cargarSpinner()
//            viewModel.getLoggetUser.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//                lbl_order_autorizadoa.setText("${it.nombres} ${it.apellidos}")
//                lbl_order_autorizadoacargo.setText("Administrador")
//            })
        }
        btn_programar_salida_fps.setOnClickListener {
            if (comprobarDatos()) {
                if (new) {
                    crearOrdenPorgramada()
                } else {
                    updatearOrdenProgramada()
                }
            }
        }
        lbl_order_conductor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                try{
                    idConductor=listaUsuarios[position]._idVehiculo
                    lbl_order_placa.setText(listaUsuarios[position].placa)
                }catch (e:Exception){

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        fps_descargar.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("${BASE_URL_AMAZON_S3}sanMiguel/ordenes/$id.pdf")))
        }
        lbl_order_fechasalida.setOnClickListener {
            tipe="salida"
            showDialogPicker()
        }
        lbl_order_fecharetorno.setOnClickListener {
            tipe= "entrada"
            showDialogPicker()
        }
        lbl_order_aprobado.setOnClickListener {
            tipe="aprobado"
            showDialogPicker()
        }
        lbl_order_fechasolicitud.setOnClickListener {
            tipe="fecha"
            showDialogPicker()
        }
    }

    private fun cargarSpinner() {
        viewModel.ConductoresSinOrdenes().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Loading -> {
                    login_progressbar.visibility=View.VISIBLE
                }
                is Resource.Success -> {
                    try {
                        val varible = mutableListOf<String>()
                        listaUsuarios.addAll(it.data)
                        if(ordenProgramada!=null){
                            if(ordenProgramada!!.conductorAutorizado.isNotEmpty()){
                                idConductor=ordenProgramada!!.carId
                                listaUsuarios.add(0,
                                    ConductoresSinOrdenes(idConductor,idConductor,ordenProgramada!!.conductorAutorizado?:"null","",ordenProgramada!!.camionetaPlaca?:"null"))
                                varible.add(0,"${ordenProgramada!!.conductorAutorizado?:"null"}")
                            }
                        }
                        for (dato in it.data) {
                            varible.add("${dato.nombres?:"null"} ${dato.apellidos?:"null"} ")
                        }
                        datos = ArrayAdapter(
                            requireContext(), // Context
                            android.R.layout.simple_spinner_item, // Layout
                            varible// Array
                        )
                        datos!!.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                        lbl_order_conductor.adapter = datos
                        login_progressbar.visibility = View.GONE
                    }catch (e:Exception){
                        Log.e("error","${e.message}")
                    }
                }
                is Resource.Failure -> {
                    Log.e("error",it.exception.message!!)
                    snakBar(it.exception.message!!)
                }
            }
        })
    }
    private fun updatearOrdenProgramada() {
        val aprobado=lbl_order_aprobado.text.toString().trim()
        val autorizadoa=lbl_order_autorizadoa.text.toString().trim()
        val autorizadoacargo=lbl_order_autorizadoacargo.text.toString().trim()
        val codigo=lbl_order_codigo.text.toString().trim()

        val conductor=lbl_order_conductor.selectedItem.toString()
        val destinosalida=lbl_order_destinosalida.text.toString().trim()
        val fechasolicitud=lbl_order_fechasolicitud.text.toString().trim()
        val formulario=lbl_order_formulario.text.toString().trim()

        val observaciones=lbl_order_observaciones.text.toString().trim()
        val ocupantes=lbl_order_ocupantes.text.toString().trim()
        val origensalida= lbl_order_origensalida.text.toString().trim()
        val placa=lbl_order_placa.text.toString().trim()
        val proyecto=lbl_order_proyecto.text.toString().trim()
        val retornodestino= lbl_order_retornodestino.text.toString().trim()

        val retornoorigen= lbl_order_retornoorigen.text.toString().trim()
        val tiempoestimado= lbl_order_tiempoestimado.text.toString().trim()
        val version= lbl_order_version.text.toString().trim()
        val Dato= OrdenProgramada(aprobado,autorizadoa,autorizadoacargo,"","",placa,codigo,
            conductor,fechasolicitud,formulario,observaciones,ocupantes,proyecto,retornodestino,fechaRetorno,horaRetorno,retornoorigen,
            destinosalida,fechaSalida,horaSalida,origensalida,tiempoestimado,version,idConductor)
        viewModel.updaterdenProgramada(Dato,id).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Loading -> {
                    login_progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    snakBar("La orden fue actualizada correctamente")
                    login_progressbar.visibility = View.GONE
                }
                is Resource.Failure -> {
                    snakBar(" ${it.exception.message}")
                    Log.e("error", it.exception.message)
                    login_progressbar.visibility = View.GONE
                }
            }
        })


    }

    private fun crearOrdenPorgramada() {
        val aprobado=lbl_order_aprobado.text.toString().trim()
        val autorizadoa=lbl_order_autorizadoa.text.toString().trim()
        val autorizadoacargo=lbl_order_autorizadoacargo.text.toString().trim()
        val codigo=lbl_order_codigo.text.toString().trim()

        val conductor=lbl_order_conductor.selectedItem.toString()
        val destinosalida=lbl_order_destinosalida.text.toString().trim()
        val fechasolicitud=lbl_order_fechasolicitud.text.toString().trim()
        val formulario=lbl_order_formulario.text.toString().trim()

        val observaciones=lbl_order_observaciones.text.toString().trim()
        val ocupantes=lbl_order_ocupantes.text.toString().trim()
        val origensalida= lbl_order_origensalida.text.toString().trim()
        val placa=lbl_order_placa.text.toString().trim()
        val proyecto=lbl_order_proyecto.text.toString().trim()
        val retornodestino= lbl_order_retornodestino.text.toString().trim()

        val retornoorigen= lbl_order_retornoorigen.text.toString().trim()
        val tiempoestimado= lbl_order_tiempoestimado.text.toString().trim()
        val version= lbl_order_version.text.toString().trim()
        val Dato= OrdenProgramada(aprobado,autorizadoa,autorizadoacargo,"","",placa,codigo,
            conductor,fechasolicitud,formulario,observaciones,ocupantes,proyecto,retornodestino,fechaRetorno,horaRetorno,retornoorigen,
            destinosalida,fechaSalida,horaSalida,origensalida,tiempoestimado,version,idConductor)
        viewModel.createOrdenProgramada(Dato).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Loading -> {
                    login_progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    snakBar("La orden fue creada correctamente")
                    login_progressbar.visibility = View.GONE
                }
                is Resource.Failure -> {
                    snakBar(" ${it.exception.message}")
                    Log.e("error", it.exception.message)
                    login_progressbar.visibility = View.GONE
                }
            }
        })
    }

    private fun comprobarDatos(): Boolean {
        val autorizadopor=lbl_order_aitorizadopor.text.toString().trim()
        val aprobado=lbl_order_aprobado.text.toString().trim()
        val autorizadoa=lbl_order_autorizadoa.text.toString().trim()
        val autorizadoacargo=lbl_order_autorizadoacargo.text.toString().trim()
        val autorizadoporcargo=lbl_order_autorizadoporcargo.text.toString().trim()
        val codigo=lbl_order_codigo.text.toString().trim()

//        lbl_order_conductor.text=data.conductorAutorizado
        val destinosalida=lbl_order_destinosalida.text.toString().trim()
        val fecharetorno=lbl_order_fecharetorno.text.toString().trim()
        val fechasalida=lbl_order_fechasalida.text.toString().trim()
        val fechasolicitud=lbl_order_fechasolicitud.text.toString().trim()
        val formulario=lbl_order_formulario.text.toString().trim()

        val observaciones=lbl_order_observaciones.text.toString().trim()
        val ocupantes=lbl_order_ocupantes.text.toString().trim()
        val origensalida= lbl_order_origensalida.text.toString().trim()
        val placa=lbl_order_placa.text.toString().trim()
        val proyecto=lbl_order_proyecto.text.toString().trim()
        val retornodestino= lbl_order_retornodestino.text.toString().trim()

        val retornoorigen= lbl_order_retornoorigen.text.toString().trim()
        val tiempoestimado= lbl_order_tiempoestimado.text.toString().trim()
        val version= lbl_order_version.text.toString().trim()
        if (autorizadopor.isEmpty()){
            lbl_order_aitorizadopor.error="Este campo esta vacio"
            lbl_order_aitorizadopor.requestFocus()
            return false
        }
        if (autorizadoporcargo.isEmpty()){
            lbl_order_autorizadoporcargo.error="Este campo esta vacio"
            lbl_order_autorizadoporcargo.requestFocus()
            return false
        }



        if (aprobado.isEmpty()){
            lbl_order_aprobado.error="Debes introducir una fecha"
            lbl_order_aprobado.requestFocus()
            return false
        }
        if (autorizadoa.isEmpty()){
            lbl_order_autorizadoa.error="Debe estar su nombre en este campo!."
            lbl_order_autorizadoa.requestFocus()
            return false
        }
        if (autorizadoacargo.isEmpty()){
            lbl_order_autorizadoacargo.error="Debes introducir tu cargo"
            lbl_order_autorizadoacargo.requestFocus()
            return false
        }
        if (codigo.isEmpty()){
            lbl_order_codigo.error="Debes introducir un codigo"
            lbl_order_codigo.requestFocus()
            return false
        }
        if (destinosalida.isEmpty()){
            lbl_order_destinosalida.error="Este campo esta vacio"
            lbl_order_destinosalida.requestFocus()
            return false
        }
        if (fecharetorno.isEmpty()){
            lbl_order_fecharetorno.error="Este campo esta vacio"
            lbl_order_fecharetorno.requestFocus()
            return false
        }
        if (fechasalida.isEmpty()){
            lbl_order_fechasalida.error="Este campo esta vacio"
            lbl_order_fechasalida.requestFocus()
            return false
        }
        if (fechasolicitud.isEmpty()){
            lbl_order_fechasolicitud.error="Este campo esta vacio"
            lbl_order_fechasolicitud.requestFocus()
            return false
        }
        if (formulario.isEmpty()){
            lbl_order_formulario.error="Este campo esta vacio"
            lbl_order_formulario.requestFocus()
            return false
        }
        if (observaciones.isEmpty()){
            lbl_order_observaciones.error="Este campo esta vacio"
            lbl_order_observaciones.requestFocus()
            return false
        }
        if (ocupantes.isEmpty()){
            lbl_order_ocupantes.error="Debes introducir una fecha"
            lbl_order_ocupantes.requestFocus()
            return false
        }
        if (origensalida.isEmpty()){
            lbl_order_origensalida.error="Debes introducir una fecha"
            lbl_order_origensalida.requestFocus()
            return false
        }
        if (placa.isEmpty()){
            lbl_order_placa.error="Debes introducir una fecha"
            lbl_order_placa.requestFocus()
            return false
        }
        if (proyecto.isEmpty()){
            lbl_order_proyecto.error="Debes introducir una fecha"
            lbl_order_proyecto.requestFocus()
            return false
        }
        if (retornodestino.isEmpty()){
            lbl_order_retornodestino.error="Debes introducir una fecha"
            lbl_order_retornodestino.requestFocus()
            return false
        }
        if (retornoorigen.isEmpty()){
            lbl_order_retornoorigen.error="Debes introducir una fecha"
            lbl_order_retornoorigen.requestFocus()
            return false
        }


        if (tiempoestimado.isEmpty()){
            lbl_order_tiempoestimado.error="Debes introducir una fecha"
            lbl_order_tiempoestimado.requestFocus()
            return false
        }
        if (version.isEmpty()){
            lbl_order_version.error="Debes introducir una fecha"
            lbl_order_version.requestFocus()
            return false
        }


        return true
    }

    private fun LoadDataNotify() {
        viewModel.getNotificationById(id).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Loading -> {
                    login_progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    UpdateData(it.data)
                    btn_programar_salida_fps.text="Actualizar Orden"
                    login_progressbar.visibility = View.GONE
                }
                is Resource.Failure -> {
                    snakBar(" ${it.exception.message}")
                    Log.e("error", it.exception.message)
                    login_progressbar.visibility = View.GONE
                }
            }
        })
    }

    private fun UpdateData(data: OrdenProgramada) {
        Log.e("dataRecibida",data.toString())
        ordenProgramada=data
        idConductor=data.carId
        fechaRetorno=data.retornoFecha
        fechaSalida=data.salidaFecha
        horaSalida=data.salidaHora
        horaRetorno=data.retornoHora
        cargarSpinner()

        lbl_order_aitorizadopor.setText( data.autorizadoPor)
        lbl_order_aprobado.text = data.aprobado
        lbl_order_autorizadoa.setText(data.autorizadoA)
        lbl_order_autorizadoacargo.setText(data.autorizadoACargo)
        lbl_order_autorizadoporcargo.setText(data.autorizadoPorCargo)
        lbl_order_codigo.setText(data.codigo)

//        lbl_order_conductor.text=data.conductorAutorizado
        lbl_order_destinosalida.setText(data.salidaDestino)
        lbl_order_fecharetorno.setText(data.retornoFecha)
        lbl_order_fechasalida.setText(data.salidaFecha)
        lbl_order_fechasolicitud.setText(data.fechaSolicitud)
        lbl_order_formulario.setText(data.formulario)

        lbl_order_observaciones.setText(data.observaciones)
        lbl_order_ocupantes.setText(data.ocupantes)
        lbl_order_origensalida.setText(data.salidaOrigen)
        lbl_order_placa.setText(data.camionetaPlaca)
        lbl_order_proyecto.setText(data.proyecto)
        lbl_order_retornodestino.setText(data.retornoDestino)

        lbl_order_retornoorigen.setText(data.retornoOrigen)
        lbl_order_tiempoestimado.setText(data.tiempoEstimadoViaje)
        lbl_order_version.setText(data.version)
    }
    var horaSalida:String=""
    var fechaSalida:String=""
    var horaRetorno:String=""
    var fechaRetorno:String=""
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        hora=hourOfDay
        minuto=minute
        when(tipe){
            "salida"->{
                horaSalida=HoraModel()
                fechaSalida=fechaModel()
                lbl_order_fechasalida.setText("${fechaModel()} - ${HoraModel()}")
            }
            "entrada"->{
                horaRetorno=HoraModel()
                fechaRetorno=fechaModel()
                lbl_order_fecharetorno.setText("${fechaModel()} - ${HoraModel()}")
            }
        }

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        anio=year
        mes=month
        dia=dayOfMonth

        when(tipe){
            "salida"->{
                showDialog()
            }
            "entrada"->{
                showDialog()
            }
            "aprobado"->{
                lbl_order_aprobado.text = fechaModel()
            }
            "fecha"->{
                lbl_order_fechasolicitud.setText(fechaModel())
            }

        }
    }
    private fun HoraModel():String{
        var horas="$hora:$minuto:00"
        if (hora<10){
            horas = if (minuto<10)
                "0$hora:0${minuto}:00"
            else
                "0$hora:${minuto}-00"
        }else{
            if (minuto<10)
                "$hora:0${minuto}:00"
        }
        return horas
    }
    private fun fechaModel():String{
        var fechaParte="$anio-${mes+1}-$dia"
        if (mes+1<10){
            fechaParte = if (dia<10)
                "$anio-0${mes+1}-0$dia"
            else
                "$anio-0${mes+1}-$dia"
        }else{
            if (dia<10)
                fechaParte="$anio-${mes+1}-0$dia"
        }
        return fechaParte
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
