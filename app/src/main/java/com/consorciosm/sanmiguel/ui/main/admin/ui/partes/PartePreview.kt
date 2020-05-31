package com.consorciosm.sanmiguel.ui.main.admin.ui.partes

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.constans.Constants.BASE_URL_AMAZON_IMG
import com.consorciosm.sanmiguel.common.utils.Resource
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

    fun SetearData(parteDiario: ParteDiario){
        lbl_fecha_fparte.text = parteDiario.infoGeneral.fechaDia
        lbl_empresaprov_fparte.text = parteDiario.infoGeneral.empresaProveedora
        lbl_empresaprov_licencia.text = parteDiario.infoGeneral.licenciaEmpresa
        lbl_horasalida_fparte.text = parteDiario.infoGeneral.salidaGaraje
        lbl_horaingreso_fparte.text = parteDiario.infoGeneral.entradaGaraje
        lbl_actividad_fparte.text = parteDiario.actividadDiaria.actividad
        lbl_hora_inicio_fparte.text = parteDiario.actividadDiaria.Horainicio
        lbl_kilometraje_inicio_fparte.text = parteDiario.actividadDiaria.Kilometrajeinicio
        lbl_hora_final_fparte.text = parteDiario.actividadDiaria.HoraFin
        lbl_kilometraje_final_fparte.text = parteDiario.actividadDiaria.KilometrajeFin
        lbl_kilometraje_abastecimiento_fparte.text = parteDiario.abastecimiento.Kilometraje
        lbl_galones_abastecimiento_fparte.text = parteDiario.abastecimiento.galones
       niveldeConbustible.setText(parteDiario.ncombustible.Ndecombustible)
        lbl_licencia_fparte.text= parteDiario.infoGeneral.licenciaEmpresa
        lbl_nombres_fparte.text="${parteDiario.chofer.nombres} ${parteDiario.chofer.apellidos}"
        lbl_marca_vehiculo_fparte.text= parteDiario.vehiculoInfo.Marca
        lbl_modelo_vehiculo_fparte.text= parteDiario.vehiculoInfo.Modelo
        lbl_placa_vehiculo_fparte.text= parteDiario.vehiculoInfo.idPlaca

        Glide.with(requireContext()).load(BASE_URL_AMAZON_IMG+parteDiario.ncombustible.imgCombustible).into(photoCombustible)
        //Estado Vehiculo

        cb_tarjeta_proiedad_fparte.isChecked=parteDiario.estadoVehiculo.tarjetaPropiedad
        cb_soat_fparte.isChecked=parteDiario.estadoVehiculo.Soat
        cb_triangulos_seguridad_fparte.isChecked=parteDiario.estadoVehiculo.ConosSeguridad
         cb_botiquin_fparte.isChecked=parteDiario.estadoVehiculo.Botiquin
        cb_nivel_aceite_fparte.isChecked=parteDiario.estadoVehiculo.NivelAceite
        cb_nivel_agua_fparte.isChecked=parteDiario.estadoVehiculo.NivelAgua
        cb_nivel_liquido_frenos_fparte.isChecked=parteDiario.estadoVehiculo.LiquidoFrenos
        cb_nivel_liquido_hidrolina_fparte.isChecked=parteDiario.estadoVehiculo.LiquidoHidrolina
        cb_espejos_fparte.isChecked=parteDiario.estadoVehiculo.Espejos
        cb_gata_palanca_fparte.isChecked=parteDiario.estadoVehiculo.GataPalanca
        cb_extintor_fparte.isChecked=parteDiario.estadoVehiculo.Extintor
        cb_luces_exteriores_fparte.isChecked=parteDiario.estadoVehiculo.LucesExteriores
         cb_nivel_refrigerante_fparte.isChecked=parteDiario.estadoVehiculo.RefrigeranteRadiador
         cb_alarma_retroceso_fparte.isChecked=parteDiario.estadoVehiculo.AlarmaRetroceso
        cb_herramientas_fparte.isChecked=parteDiario.estadoVehiculo.Herramientas

    }
}