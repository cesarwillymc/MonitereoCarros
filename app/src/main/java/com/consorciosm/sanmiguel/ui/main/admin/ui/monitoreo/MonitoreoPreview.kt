package com.consorciosm.sanmiguel.ui.main.admin.ui.monitoreo

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.RutaProgramada
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_monitoreo_preview.*
import org.jetbrains.anko.backgroundColor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*


class MonitoreoPreview : BaseFragment(),KodeinAware, DatePickerDialog.OnDateSetListener  {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    lateinit var googleMap:GoogleMap
    private var currentPoline: Polyline?=null
    private var idConductor=""



    var anio= Calendar.getInstance().get(Calendar.YEAR)
    var mes= Calendar.getInstance().get(Calendar.MONTH)
    var dia= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap=googleMap
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        googleMap!!.isMyLocationEnabled=true
        googleMap.uiSettings.isMyLocationButtonEnabled=false
        moveMapCamera()
    }


    override fun getLayout(): Int = R.layout.fragment_monitoreo_preview
    var identificador=""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        fecha.setText(fechaModel())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(callback)
        idConductor=MonitoreoPreviewArgs.fromBundle(requireArguments()).id

        fecha.setOnClickListener {
            identificador="inicio"
            showDialogPicker()
        }
//        fechaFinal.setOnClickListener {
//            identificador="final"
//            showDialogPicker()
//        }
        buscarProDatos()
        searchFecha.setOnClickListener {

            buscarProDatos()
        }

    }
    fun buscarProDatos(){
        val inicio= fecha.text.toString().trim()
        val final= fechaFinal.text.toString().trim()
        viewModel.getDataRecorridoConductor(idConductor,inicio,final).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    hideKeyboard()
                    progressbar.visibility=View.VISIBLE
                }
                is Resource.Success->{
                    progressbar.visibility=View.GONE
                    cargarData(it.data)
                }
                is Resource.Failure->{
                    progressbar.visibility=View.GONE
                    snakBar(it.exception.message!! + " Reinicia la app")
                }
            }
        })
    }
    private fun cargarData(data: RutaProgramada) {
        Log.e("datos",data.toString())
        fmp_txt_color.backgroundColor= data.color.toInt()
        fmp_txt_kilometros.text=data.kilometros
        fmp_txt_name.text= data.nombres
        fmp_txt_phone.text= data.celular
        fmp_txt_placa.text= data.placa
        val listaPuntos= mutableListOf<LatLng>()
        val inicio=LatLng(data.inicioRecorrido.latitude,data.inicioRecorrido.longitude)
        val final=LatLng(data.finalRecorrido.latitude,data.finalRecorrido.longitude)

        for (value in data.recorrido){
            listaPuntos.add(LatLng(value.latitude,value.longitude))
        }
        setDataFindView(listaPuntos,inicio,final)
    }
    private val PATTERN_DASH_LENGTH_PX = 20
    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT: PatternItem = Dot()
    private val DASH: PatternItem = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
    private  val PATTERN_POLYGON_ALPHA = listOf(GAP, DASH)
    private fun setDataFindView(
        data: List<LatLng>,
        inicio: LatLng,
        final: LatLng
    ) {

        try{
            googleMap.addMarker(
                MarkerOptions().icon(bitmapDescriptorFromVector(requireContext(), R.drawable.termino))
                    .title("Destino")
                    .position(final))
            googleMap!!.addMarker(
                MarkerOptions().icon(bitmapDescriptorFromVector(requireContext(),R.drawable.inicio))
                    .title("Origen")
                    .position(inicio ))
            try {
                val cameramove= LatLngBounds(final,inicio)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(cameramove,50))
                Log.e("latLong","origen")
            }catch (e:Exception){
                val cameramove= LatLngBounds(inicio,final)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(cameramove,50))
                Log.e("latLong",e.message)
            }
            var lineOptions: PolylineOptions? = PolylineOptions()
            lineOptions!!.addAll(data)
            lineOptions.width(10f)
            lineOptions.color(Color.MAGENTA)
            lineOptions.pattern(PATTERN_POLYGON_ALPHA)
            lineOptions.geodesic(true)
            currentPoline=googleMap!!.addPolyline(lineOptions)
        }catch (e:Exception){
            Log.e("Error",e.message)
        }
//        googleMap.uiSettings.isTiltGesturesEnabled=false
//        googleMap.uiSettings.isScrollGesturesEnabled=false
//        googleMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom=false
//        googleMap.uiSettings.isZoomControlsEnabled=false
    }
    private fun moveMapCamera() {

        val center = CameraUpdateFactory.newLatLng( LatLng(-15.834, -70.019))
        val zoom = CameraUpdateFactory.zoomTo(18f)

        googleMap!!.moveCamera(center)
        googleMap!!.animateCamera(zoom)
    }
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        anio=year
        mes=month
        dia=dayOfMonth
        if(identificador=="inicio"){
            fecha.setText(fechaModel())
        }else{
            fechaFinal.setText(fechaModel())
        }

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

    private fun showDialogPicker() {
        val datepicker= DatePickerDialog(requireContext(),this,
            anio,
            mes,
            dia)
        datepicker.show()
    }
}