package com.consorciosm.sanmiguel.ui.main.admin.ui.registro_carros

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.constans.Constants.BASE_URL_AMAZON_IMG
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.common.utils.detectar_formato
import com.consorciosm.sanmiguel.common.utils.getPath
import com.consorciosm.sanmiguel.data.model.UsuarioList
import com.consorciosm.sanmiguel.data.model.VehiculoCreate
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_registro_carros.*
import org.jetbrains.anko.textColor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import yuku.ambilwarna.AmbilWarnaDialog
import java.io.File

class RegistroCarrosFragment : BaseFragment(), KodeinAware {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()

    var file: File? = null

    var new = false
    var id = ""
    var idConductor =""
    var listaUsuarios = mutableListOf<UsuarioList>()
    var  datos: ArrayAdapter<String>?=null
    var vehiculoCreate:VehiculoCreate?=null

    var   mDefaultColor= 0
    override fun getLayout(): Int = R.layout.fragment_registro_carros
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = requireActivity().run {
            ViewModelProvider(this, factory).get(ViewModelMain::class.java)
        }
        mDefaultColor=ContextCompat.getColor(requireContext(),R.color.colorAccent)
        new = RegistroCarrosFragmentArgs.fromBundle(requireArguments()).new
        id = RegistroCarrosFragmentArgs.fromBundle(requireArguments()).id

        if (!new) {
            btn_registrar_frc.text = "Actualizar datos!!!"
            getData()
        }else{
            cargarSpinner()
        }

        Dexter.withContext(requireContext()).withPermissions(
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                img_vehiculo_frc.setOnClickListener {
                    registroVehiculo()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {

            }

        }).check()
        et_color_frc.setOnClickListener {
            openColorPicker()

        }
        spn_conductor_frc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                try{
                    idConductor=listaUsuarios[position]._id
                }catch (e:Exception){

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        btn_registrar_frc.setOnClickListener {
            if (comprobarDatos()) {
                if (new) {
                    if (file != null) {
                        sendVlaues()
                    } else {
                        snakBar("Introduce una imagen")
                    }
                } else {
                    updateVehiculo()
                }
            }

        }
    }

    private fun openColorPicker() {
        val colorPicker= AmbilWarnaDialog(requireContext(),mDefaultColor, object:AmbilWarnaDialog.OnAmbilWarnaListener{
            override fun onCancel(dialog: AmbilWarnaDialog?) {}
            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                mDefaultColor = color
                txt_color_frc.textColor = color
                et_color_frc.setTextColor(color)
                et_color_frc.setText("$mDefaultColor")
            }

        } )
        colorPicker.show()
    }

    private fun cargarSpinner() {
        viewModel.conductoresSinVehiculo().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    snakBar("Cargando COnductores")
                }
                is Resource.Success -> {
                    Log.e("error","$vehiculoCreate")
                    Log.e("error","${it.data}")
                    try {
                        val varible = mutableListOf<String>()
                        listaUsuarios.addAll(it.data)
                        if(vehiculoCreate!=null){
                            if(vehiculoCreate!!.conductor.isNotEmpty()){
                                idConductor=vehiculoCreate!!.conductor
                                listaUsuarios.add(0,UsuarioList(vehiculoCreate!!.conductor?:"null","",vehiculoCreate!!.nombres?:"null",vehiculoCreate!!.apellidos?:"null",""))
                                varible.add(0,"${vehiculoCreate!!.nombres?:"null"} ${vehiculoCreate!!.apellidos?:"null"}")
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
                        spn_conductor_frc.adapter = datos
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

    private fun getData() {
        viewModel.getInfoCarroById(id).observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    login_progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {

                    LoadData(it.data)


                }
                is Resource.Failure -> {
                    login_progressbar.visibility = View.GONE
                    snakBar(it.exception.message!!)
                }
            }
        })
    }

    private fun LoadData(data: VehiculoCreate) {
            vehiculoCreate=data
        Glide.with(requireContext()).load(BASE_URL_AMAZON_IMG+data.imagenCarro).into(img_vehiculo_frc)
        et_nro_placa_frc.setText(data.numeroPlaca)
        et_nro_serie_frc.setText(data.numeroSerie)
        et_nro_vin_frc.setText(data.numeroVin)
        et_nro_motor_frc.setText(data.numeroMotor)

        //Color carro
        mDefaultColor = data.color.toInt()
        txt_color_frc.textColor = data.color.toInt()
        et_color_frc.setTextColor(data.color.toInt())
        et_color_frc.setText("$mDefaultColor")

        et_marca_frc.setText(data.marca)
        et_modelo_frc.setText(data.modelo)

        et_placa_vigente_frc.setText(data.placaVigente)
        et_placa_anterior_frc.setText(data.placaAnterior)
        et_estado_frc.setText(data.estado)
        et_anotaciones.setText(data.anotaciones)
        et_sede.setText(data.sede)
        et_kilometros.setText(data.kilometros)
        et_Conbustible.setText(data.nivelCombustible)
        cargarSpinner()
    }

    private fun updateVehiculo() {
        val placa = et_nro_placa_frc.text.toString().trim()

        val serie = et_nro_serie_frc.text.toString().trim()
        val vin = et_nro_vin_frc.text.toString().trim()
        val motor = et_nro_motor_frc.text.toString().trim()
        val color = et_color_frc.text.toString().trim()
        val marca = et_marca_frc.text.toString().trim()
        val modelo = et_modelo_frc.text.toString().trim()

        val vigente = et_placa_vigente_frc.text.toString().trim()
        val anterior = et_placa_anterior_frc.text.toString().trim()
        val estado = et_estado_frc.text.toString().trim()
        val anotaciones = et_anotaciones.text.toString().trim()
        val sede = et_sede.text.toString().trim()
        val kilometros = et_kilometros.text.toString().trim()
        val combustible = et_Conbustible.text.toString().trim()


        viewModel.updateVehiculo(
            VehiculoCreate(
                placa,
                idConductor,
                serie,
                vin,
                motor,
                color,
                marca,
                modelo,
                vigente,
                anterior,
                estado,
                anotaciones,
                sede,
                kilometros,
                combustible
            ), id
        ).observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    hideKeyboard()
                    login_progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    snakBar("Datos actualizados correctamente")
                    login_progressbar.visibility = View.GONE
                }
                is Resource.Failure -> {
                    login_progressbar.visibility = View.GONE
                    snakBar(it.exception.message!!)
                }
            }
        })
    }

    private fun sendVlaues() {
        val placa = et_nro_placa_frc.text.toString().trim()
        val serie = et_nro_serie_frc.text.toString().trim()
        val vin = et_nro_vin_frc.text.toString().trim()
        val motor = et_nro_motor_frc.text.toString().trim()
        val color = et_color_frc.text.toString().trim()
        val marca = et_marca_frc.text.toString().trim()
        val modelo = et_modelo_frc.text.toString().trim()

        val vigente = et_placa_vigente_frc.text.toString().trim()
        val anterior = et_placa_anterior_frc.text.toString().trim()
        val estado = et_estado_frc.text.toString().trim()
        val anotaciones = et_anotaciones.text.toString().trim()
        val sede = et_sede.text.toString().trim()
        val kilometros = et_kilometros.text.toString().trim()
        val combustible = et_Conbustible.text.toString().trim()
        viewModel.createVehiculo(
            VehiculoCreate(
                placa,
                idConductor,
                serie,
                vin,
                motor,
                color,
                marca,
                modelo,
                vigente,
                anterior,
                estado,
                anotaciones,
                sede,
                kilometros,
                combustible
            )
            , file!!, "carImg"
        ).observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    hideKeyboard()
                    login_progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    clearData()
                    snakBar("Datos guardados correctamente")
                    login_progressbar.visibility = View.GONE
                }
                is Resource.Failure -> {
                    login_progressbar.visibility = View.GONE
                    snakBar(it.exception.message!!)
                }
            }
        })
    }

    private fun clearData() {

        img_vehiculo_frc.setImageDrawable(requireContext().getDrawable(R.drawable.ic_launcher_background))
        et_nro_placa_frc.setText("")
        et_nro_serie_frc.setText("")
        et_nro_vin_frc.setText("")
        et_nro_motor_frc.setText("")
        et_color_frc.setText("")
        et_marca_frc.setText("")
        et_modelo_frc.setText("")

        et_placa_vigente_frc.setText("")
        et_placa_anterior_frc.setText("")
        et_estado_frc.setText("")
        et_anotaciones.setText("")
        et_sede.setText("")

    }

    private fun comprobarDatos(): Boolean {
        val placa = et_nro_placa_frc.text.toString().trim()
        val serie = et_nro_serie_frc.text.toString().trim()
        val vin = et_nro_vin_frc.text.toString().trim()
        val motor = et_nro_motor_frc.text.toString().trim()
        val color = et_color_frc.text.toString().trim()
        val marca = et_marca_frc.text.toString().trim()
        val modelo = et_modelo_frc.text.toString().trim()

        val vigente = et_placa_vigente_frc.text.toString().trim()
        val estado = et_estado_frc.text.toString().trim()
        val anotaciones = et_anotaciones.text.toString().trim()
        val sede = et_sede.text.toString().trim()
        val kilometros = et_kilometros.text.toString().trim()
        val combustible = et_Conbustible.text.toString().trim()
        if(listaUsuarios.isEmpty()){
            snakBar("Refresca la ventana por favor!.")
            return false
        }
        if (placa.isEmpty()) {
            et_nro_placa_frc.error = "Placa esta vacio!"
            et_nro_placa_frc.requestFocus()
            return false
        }
        if (serie.isEmpty()) {
            et_nro_serie_frc.error = "Serie esta vacio!"
            et_nro_serie_frc.requestFocus()
            return false
        }
        if (vin.isEmpty()) {
            et_nro_vin_frc.error = "Vin esta vacio!"
            et_nro_vin_frc.requestFocus()
            return false
        }
        if (motor.isEmpty()) {
            et_nro_motor_frc.error = "Motor esta vacio!"
            et_nro_motor_frc.requestFocus()
            return false
        }
        if (color.isEmpty()) {
            et_color_frc.error = "Color esta vacio!"
            et_color_frc.requestFocus()
            return false
        }
        if (marca.isEmpty()) {
            et_marca_frc.error = "Marca esta vacio!"
            et_marca_frc.requestFocus()
            return false
        }
        if (modelo.isEmpty()) {
            et_modelo_frc.error = "Modelo esta vacio!"
            et_modelo_frc.requestFocus()
            return false
        }
        if (vigente.isEmpty()) {
            et_placa_vigente_frc.error = "Placa vigente esta vacio!"
            et_placa_vigente_frc.requestFocus()
            return false
        }

        if (estado.isEmpty()) {
            et_estado_frc.error = "Estado esta vacio!"
            et_estado_frc.requestFocus()
            return false
        }
        if (anotaciones.isEmpty()) {
            et_anotaciones.error = "Anotaciones esta vacio!"
            et_anotaciones.requestFocus()
            return false
        }
        if (sede.isEmpty()) {
            et_sede.error = "Sede esta vacio!"
            et_sede.requestFocus()
            return false
        }
        if (kilometros.isEmpty()) {
            et_kilometros.error = "kilometros esta vacio!"
            et_kilometros.requestFocus()
            return false
        }
        if (combustible.isEmpty()) {
            et_Conbustible.error = "combustible esta vacio!"
            et_Conbustible.requestFocus()
            return false
        }
        if (placa.length != 6) {
            et_nro_placa_frc.error = "La placa  tiene 6 numeros!"
            et_nro_placa_frc.requestFocus()
            return false
        }
        if (vigente.length != 6) {
            et_placa_vigente_frc.error = "La placa vigente  tiene 6 numeros!"
            et_placa_vigente_frc.requestFocus()
            return false
        }
        if (vin != serie) {
            et_nro_serie_frc.error = "El numero de serie y vin son identicos"
            et_nro_vin_frc.error = "El numero de serie y vin son identicos"
            return false
        }
        return true
    }

    fun registroVehiculo() {
        val opciones =
            arrayOf<CharSequence>("Tomar Foto", "Cargar Imagen")
        val alertOpciones =
            AlertDialog.Builder(requireContext())

        alertOpciones.setTitle("Seleccione una opción:")
        alertOpciones.setItems(
            opciones
        ) { dialogInterface, i ->
            try {
                if (opciones[i] == "Tomar Foto") {
                    file = null
                    val builder = VmPolicy.Builder()
                    StrictMode.setVmPolicy(builder.build())
                    val directoryPath =
                        Environment.getExternalStorageDirectory().toString() + "/" + "evanstechnologiesdriver" + "/"
                    val filePath =
                        directoryPath + java.lang.Long.toHexString(System.currentTimeMillis()) + ".jpeg"
                    val data = File(directoryPath)
                    if (!data.exists()) {
                        data.mkdirs()
                    }
                    file = File(filePath)

                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(File(filePath))
                    )
                    startActivityForResult(intent, 20)
                } else {
                    file = null
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/"
                    startActivityForResult(
                        Intent.createChooser(
                            intent,
                            "Seleccione la Aplicación"
                        ), 10
                    )
                }
            } catch (e: Exception) {
                Log.e("errorcamera", e.message)
            }
        }

        alertOpciones.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                10 -> {
                    val value = data!!.data
                    val dato = File(requireActivity().getPath(value!!))
                    //setea a la imagen
                    if (detectar_formato(dato.path) != "ninguno") {
                        file = dato
                        if (new) {
                            img_vehiculo_frc.setImageURI(value)
                        } else {
                            updatePhoto(file!!)
                        }
                    } else {

                        snakBar("Formato de imagen no valido")
                    }


                }

                20 -> {
                    if (file!!.exists()) {
                        val value = Uri.fromFile(file)
                        if (new) {
                            img_vehiculo_frc.setImageURI(value)
                        } else {
                            updatePhoto(file!!)
                        }

                        //setea a la imagen
                    }
                }
            }
        }
    }

    private fun updatePhoto(file: File) {
        viewModel.updatePhoto(file, "carImg", id).observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    login_progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    img_vehiculo_frc.setImageURI(Uri.fromFile(file))
                    snakBar("Photo actualizada correctamente")
                    login_progressbar.visibility = View.GONE
                }
                is Resource.Failure -> {
                    login_progressbar.visibility = View.GONE
                    snakBar(it.exception.message!!)
                }
            }
        })
    }
}
