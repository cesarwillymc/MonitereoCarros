package com.consorciosm.sanmiguel.ui.main.admin.ui.partes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.constans.Constants
import com.consorciosm.sanmiguel.common.constans.Constants.BASE_URL_AMAZON_IMG
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.Parte
import com.consorciosm.sanmiguel.data.model.ParteDiario
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.fragment_parte_diario_detail.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class PartePreview:BaseFragment(),KodeinAware {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    var id:String=""
    override fun getLayout(): Int = R.layout.fragment_parte_diario_detail
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        if(PartePreviewArgs.fromBundle(requireArguments()).isValid){
            descargarParte.visibility=View.VISIBLE
        }
        id= PartePreviewArgs.fromBundle(requireArguments()).id
        viewModel.getparteId(id).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    login_progressbar.visibility= View.VISIBLE
                }
                is Resource.Success->{
                    SetearData(it.data)
                    login_progressbar.visibility= View.GONE
                }
                is Resource.Failure->{
                    snakBar(" ${it.exception.message}")
                    Log.e("error", it.exception.message)
                    login_progressbar.visibility= View.GONE
                }
            }
        })
        sendParte.setOnClickListener {
            validarParte()
        }
        descargarParte.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("${Constants.BASE_URL_AMAZON_S3}sanMiguel/partes/$id.pdf")))
        }
    }

    private fun validarParte() {
        viewModel.validarParte(id).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    login_progressbar.visibility= View.VISIBLE
                }
                is Resource.Success->{
                    snakBar(" El parte se valido correctamente")
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

    fun SetearData(parteDiario: Parte){
        Log.e("parteDiario",parteDiario.toString())
        lbl_fecha_fparte.text = parteDiario.parte.fechaDia
        lbl_empresaprov_fparte.text = parteDiario.parte.empresaProveedora
        lbl_empresaprov_licencia.text = parteDiario.parte.licenciaEmpresa
        lbl_horasalida_fparte.text = parteDiario.parte.salidaGaraje
        lbl_horaingreso_fparte.text = parteDiario.parte.entradaGaraje
        lbl_actividad_fparte.text = parteDiario.parte.actividad
        lbl_hora_inicio_fparte.text = parteDiario.parte.Horainicio
        lbl_kilometraje_inicio_fparte.text = parteDiario.parte.Kilometrajeinicio
        lbl_hora_final_fparte.text = parteDiario.parte.HoraFin
        lbl_kilometraje_final_fparte.text = parteDiario.parte.KilometrajeFin
        lbl_kilometraje_abastecimiento_fparte.text = parteDiario.parte.Kilometraje
        lbl_galones_abastecimiento_fparte.text = parteDiario.parte.galones
       niveldeConbustible.setText(parteDiario.parte.Ndecombustible)
        lbl_licencia_fparte.text= parteDiario.parte.licenciaEmpresa
        lbl_nombres_fparte.text="${parteDiario.conductor.nombres} ${parteDiario.conductor.apellidos}"
        lbl_marca_vehiculo_fparte.text= parteDiario.vehiculo.marca
        lbl_modelo_vehiculo_fparte.text= parteDiario.vehiculo.modelo
        lbl_placa_vehiculo_fparte.text= parteDiario.vehiculo.numeroPlaca

        Glide.with(requireContext()).load(BASE_URL_AMAZON_IMG+parteDiario.parte.imgNivelCombustible).into(photoCombustible)
        //Estado Vehiculo

        cb_tarjeta_proiedad_fparte.isChecked=parteDiario.parte.tarjetaPropiedad
        cb_soat_fparte.isChecked=parteDiario.parte.Soat
        cb_triangulos_seguridad_fparte.isChecked=parteDiario.parte.ConosSeguridad
         cb_botiquin_fparte.isChecked=parteDiario.parte.Botiquin
        cb_nivel_aceite_fparte.isChecked=parteDiario.parte.NivelAceite
        cb_nivel_agua_fparte.isChecked=parteDiario.parte.NivelAgua
        cb_nivel_liquido_frenos_fparte.isChecked=parteDiario.parte.LiquidoFrenos
        cb_nivel_liquido_hidrolina_fparte.isChecked=parteDiario.parte.LiquidoHidrolina
        cb_espejos_fparte.isChecked=parteDiario.parte.Espejos
        cb_gata_palanca_fparte.isChecked=parteDiario.parte.GataPalanca
        cb_extintor_fparte.isChecked=parteDiario.parte.Extintor
        cb_luces_exteriores_fparte.isChecked=parteDiario.parte.LucesExteriores
         cb_nivel_refrigerante_fparte.isChecked=parteDiario.parte.RefrigeranteRadiador
         cb_alarma_retroceso_fparte.isChecked=parteDiario.parte.AlarmaRetroceso
        cb_herramientas_fparte.isChecked=parteDiario.parte.Herramientas

    }
}