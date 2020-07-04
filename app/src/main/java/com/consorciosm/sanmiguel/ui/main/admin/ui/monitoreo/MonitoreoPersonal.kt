package com.consorciosm.sanmiguel.ui.main.admin.ui.monitoreo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.PuntosFirebase
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_monitoreo_personal.*
import org.jetbrains.anko.backgroundColor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


/**
 * A simple [Fragment] subclass.
 */
@SuppressLint("SetTextI18n")
class MonitoreoPersonal : BaseFragment() , KodeinAware{
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    private lateinit var googleMap: GoogleMap
    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap=googleMap
        googleMap!!.isMyLocationEnabled=true
//        googleMap.uiSettings.isMyLocationButtonEnabled=false
        moveMapCamera()
        googleMap.setOnInfoWindowClickListener {
            Toast.makeText(
                activity,
                "Infowindow clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
        googleMap.setOnMarkerClickListener {
            Toast.makeText(activity, "Marker Clicked", Toast.LENGTH_SHORT).show()
            val puntosFirebase:PuntosFirebase= it!!.tag as PuntosFirebase
            val dato=MonitoreoPersonalDirections.actionNavPersonalToDialogInfoConductor(puntosFirebase)
            findNavController().navigate(dato)

            false
        }
    }
    override fun getLayout(): Int =R.layout.fragment_monitoreo_personal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment?
        mapFragment!!.onCreate( savedInstanceState )
        mapFragment.onResume()
        mapFragment.getMapAsync(callback)

        viewModel.markersTimeReal.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{ }
                is Resource.Success->{
                    try {

                        if (it.data.isEmpty()){
                            try { googleMap.clear() }catch (e:Exception){
                                Log.e("markerE",e.message)
                            }
                        }
                        lbl_vehiculos_fmp.text="  ${it.data.size} Vehiculos Operativos"
                        Log.e("LoadData",it.data[0].color.toString())
                        cargarData(it.data)
                    }catch (e:Exception){

                    }

                }
                is Resource.Failure->{
                    snakBar(it.exception.message!! + " Reinicia la app")
                }
            }

        })
    }

    private fun cargarData(data: List<PuntosFirebase>) {
         try{
             try { googleMap.clear() }catch (e:Exception){
                 Log.e("markerE",e.message)
             }
//             val icon = BitmapFactory.decodeResource(
//                 requireContext().resources,
//                 R.drawable.ic_moto
//             )
             for (values in data){
                 val marker=googleMap.addMarker(
                     MarkerOptions()
                         .position(LatLng(values.latitude, values.longitude))
                         .icon(getBitmapFromView("${values.placa} \n ${values.state}",values.color))
                 )
                 marker.tag=values
//                 marker.showInfoWindow()
             }

         }catch (e:Exception){
             Log.e("error",e.message)
         }
    }
    private fun changeBitmapColor(sourceBitmap: Bitmap, color: Int): Bitmap? {
        val resultBitmap = sourceBitmap.copy(sourceBitmap.config, true)
        val paint = Paint()
        val filter: ColorFilter = LightingColorFilter(color, 1)
        paint.colorFilter = filter
        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(resultBitmap, 0f, 0f, paint)
        return resultBitmap
    }
    private fun moveMapCamera() {

        val center = CameraUpdateFactory.newLatLng( LatLng(-15.834, -70.019))
        val zoom = CameraUpdateFactory.zoomTo(14f)

        googleMap.moveCamera(center)
        googleMap.animateCamera(zoom)
    }
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    fun getBitmapFromView(title: String?,color:Int): BitmapDescriptor? {
        val  vista = LayoutInflater.from(requireActivity().applicationContext).inflate(R.layout.marker_tools, null)!!
        vista.findViewById<TextView>(R.id.tooltips_title).text= title
        vista.findViewById<TextView>(R.id.tooltips_title).backgroundColor= color
        vista.findViewById<ImageView>(R.id.tooltips_img).setImageDrawable(requireContext().getDrawable(R.drawable.coche))

        val width = vista.resources.getDimensionPixelSize(R.dimen.tooltips_width)
        val height = vista.resources.getDimensionPixelSize(R.dimen.tooltips_height)
        val measuredWidth = View.MeasureSpec.makeMeasureSpec(
            width,
            View.MeasureSpec.EXACTLY
        )
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(
            height,
            View.MeasureSpec.EXACTLY
        )
        vista.measure(measuredWidth, measuredHeight)
        vista.layout(0, 0, vista.measuredWidth, vista.measuredHeight)

        //Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        vista.draw(canvas)
//        val bitmap =   BitmapFactory.decodeResource(
//            requireContext().resources,
//            R.drawable.coche
//        )
//        val workingBitmap = Bitmap.createBitmap(bitmap)
//        val mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true)
//        val canvas = Canvas(mutableBitmap)
//        vista.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap (bitmap)
    }
}
