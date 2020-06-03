package com.consorciosm.sanmiguel.ui.main.supervisor.notificaciones

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.constans.Constants
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.OrdenProgramada
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.fragment_validacion_preview.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class ValidacionPreview : BaseFragment(),KodeinAware {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    var id:String=""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        id= ValidacionPreviewArgs.fromBundle(requireArguments()).id
        if (ValidacionPreviewArgs.fromBundle(requireArguments()).isValid)
            fvp_descargar.visibility=View.VISIBLE
        viewModel.getNotificationByIdSupervisor(id).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    login_progressbar.visibility= View.VISIBLE
                }
                is Resource.Success->{

                    funLoadData(it.data)
                    login_progressbar.visibility= View.GONE
                }
                is Resource.Failure->{
                    snakBar(" ${it.exception.message}")
                    Log.e("error", it.exception.message)
                    login_progressbar.visibility= View.GONE
                }
            }
        })
        btn_validar_salida_fnd.setOnClickListener {
            validarPrograma()
        }
        fvp_descargar.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("${Constants.BASE_URL_AMAZON_S3}sanMiguel/ordenes/$id.pdf")))
        }
    }

    private fun validarPrograma() {
        viewModel.validateOrden(id).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    login_progressbar.visibility= View.VISIBLE
                }
                is Resource.Success->{
                    fvp_descargar.visibility=View.VISIBLE
                    snakBar(" Validado Correctamente")
                    login_progressbar.visibility= View.GONE
                }
                is Resource.Failure->{
                    snakBar(" ${it.exception.message}")
                    Log.e("error", it.exception.message)
                    login_progressbar.visibility= View.GONE
                }
            }
        })
    }

    private fun funLoadData(data: OrdenProgramada) {
//        viewModel.getLoggetUser.observe(viewLifecycleOwner, Observer {
//            lbl_order_aitorizadopor.text="${it.nombres} ${it.apellidos}"
//            lbl_order_autorizadoporcargo.text="Supervisor"
//        })
        lbl_order_aitorizadopor.text= data.autorizadoPor
        lbl_order_autorizadoporcargo.text=data.autorizadoPorCargo
        lbl_order_aprobado.text=data.aprobado
        lbl_order_autorizadoa.text=data.autorizadoA
        lbl_order_autorizadoacargo.text=data.autorizadoACargo

        lbl_order_codigo.text=data.codigo

        lbl_order_conductor.text=data.conductorAutorizado
        lbl_order_destinosalida.text=data.salidaDestino
        lbl_order_fecharetorno.text=data.retornoFecha
        lbl_order_fechasalida.text=data.salidaFecha
        lbl_order_fechasolicitud.text=data.fechaSolicitud
        lbl_order_formulario.text=data.formulario

        lbl_order_observaciones.text=data.observaciones
        lbl_order_ocupantes.text=data.ocupantes
        lbl_order_origensalida.text=data.salidaOrigen
        lbl_order_placa.text=data.camionetaPlaca
        lbl_order_proyecto.text=data.proyecto
        lbl_order_retornodestino.text=data.retornoDestino

        lbl_order_retornoorigen.text=data.retornoOrigen
        lbl_order_tiempoestimado.text=data.tiempoEstimadoViaje
        lbl_order_version.text=data.version


    }
    override fun getLayout(): Int = R.layout.fragment_validacion_preview

}
