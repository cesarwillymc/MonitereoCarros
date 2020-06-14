package com.consorciosm.sanmiguel.ui.main.admin.ui.registro_personal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.constans.Constants.BASE_URL_AMAZON_IMG
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.common.utils.detectar_formato
import com.consorciosm.sanmiguel.common.utils.getPath
import com.consorciosm.sanmiguel.data.model.PersonalData
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.fragment_registro_personal.*
import kotlinx.android.synthetic.main.fragment_registro_personal.login_progressbar
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.File

class RegistroPersonalFragment : BaseFragment(),KodeinAware {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    override fun getLayout(): Int =R.layout.fragment_registro_personal
    var new=false
    var id=""
    var file: File? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        new=RegistroPersonalFragmentArgs.fromBundle(requireArguments()).new
        id= RegistroPersonalFragmentArgs.fromBundle(requireArguments()).id
        if (!new){
            viewModel.getInfoUserById(id).observe(viewLifecycleOwner,
                Observer {
                    when(it){
                        is Resource.Loading->{
                            login_progressbar.visibility= View.VISIBLE
                        }
                        is Resource.Success->{

                            snakBar("Datos guardados correctamente")
                            login_progressbar.visibility= View.GONE
                            setData(it.data)
                        }
                        is Resource.Failure->{
                            login_progressbar.visibility= View.GONE
                            snakBar(it.exception.message!!)
                        }
                    }
                })
        }
        et_photo_frp.setOnClickListener {
            registroVehiculo()
        }
        btn_registrar_frp.setOnClickListener {
            if (comprobarDatos()){

                if (new){
                    if (file != null) {
                        crearPersonal()
                    } else {
                        snakBar("Introduce una imagen")
                    }

                }else{
                    updateValues()
                }

            }else{
                snakBar("Datos ingresados no validos.")
            }
        }
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
                    val builder = StrictMode.VmPolicy.Builder()
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
    private fun updateValues() {
        val apellidos= et_apellidos_frp.text.toString().trim()
        val dni= et_dni_frp.text.toString().trim()
        val nombre= et_nombre_frp.text.toString().trim()
        val direccion= et_direccion_frp.text.toString().trim()
        val number0= et_telefono_frp.text.toString().trim()
        val number1= et_firstnum_frp.text.toString().trim()
        val number2= et_secondnum_frp.text.toString().trim()
        val licencia= et_licencia_frp.text.toString().trim()
        viewModel.updatePersonal(PersonalData(dni,nombre,direccion,apellidos,number0,number1,number2,licencia),id).observe(viewLifecycleOwner,
            Observer {
                when(it){
                    is Resource.Loading->{
                        hideKeyboard()
                        login_progressbar.visibility= View.VISIBLE
                    }
                    is Resource.Success->{
                        snakBar("Datos actualizados correctamente")
                        login_progressbar.visibility= View.GONE
                    }
                    is Resource.Failure->{
                        login_progressbar.visibility= View.GONE
                        snakBar(it.exception.message!!)
                    }
                }
            })
    }

    private fun setData(data: PersonalData) {
        et_licencia_frp.setText(data.licencia)
        btn_registrar_frp.text="Actualizar datos"
        textView9.visibility=View.VISIBLE
        et_pass_frp.visibility=View.VISIBLE
        et_pass_frp.setText(data.password)
        et_apellidos_frp.setText(data.apellidos)
        et_dni_frp.setText(data.dni)
        et_nombre_frp.setText(data.nombres)
        et_direccion_frp.setText(data.direccion)
        et_telefono_frp.setText(data.telefono)
        et_firstnum_frp.setText(data.telefonoReferenciaA)
        et_secondnum_frp.setText(data.telefonoReferenciaB)
        Glide.with(requireContext()).load(BASE_URL_AMAZON_IMG+data.imgLicencia).into(et_photo_frp)

    }

    private fun crearPersonal() {
        val apellidos= et_apellidos_frp.text.toString().trim()
        val dni= et_dni_frp.text.toString().trim()
        val nombre= et_nombre_frp.text.toString().trim()
        val direccion= et_direccion_frp.text.toString().trim()
        val number0= et_telefono_frp.text.toString().trim()
        val number1= et_firstnum_frp.text.toString().trim()
        val number2= et_secondnum_frp.text.toString().trim()
        val licencia= et_licencia_frp.text.toString().trim()
        viewModel.createPersonal(PersonalData(dni,nombre,direccion,apellidos,number0,number1,number2,licencia), file!!, "userLicence").observe(viewLifecycleOwner,
            Observer {
                when(it){
                    is Resource.Loading->{
                        hideKeyboard()
                        login_progressbar.visibility= View.VISIBLE
                    }
                    is Resource.Success->{
                        clearData()
                        snakBar("Datos guardados correctamente")
                        login_progressbar.visibility= View.GONE
                    }
                    is Resource.Failure->{
                        login_progressbar.visibility= View.GONE
                        snakBar(it.exception.message!!)
                    }
                }
            })
    }

    private fun clearData() {
        et_apellidos_frp.setText("")
        et_dni_frp.setText("")
        et_nombre_frp.setText("")
        et_direccion_frp.setText("")
        et_telefono_frp.setText("")
        et_firstnum_frp.setText("")
        et_secondnum_frp.setText("")
    }

    private fun comprobarDatos(): Boolean {
        val apellidos= et_apellidos_frp.text.toString().trim()
        val dni= et_dni_frp.text.toString().trim()
        val nombre= et_nombre_frp.text.toString().trim()
        val direccion= et_direccion_frp.text.toString().trim()
        val number0= et_telefono_frp.text.toString().trim()
        val licencia= et_licencia_frp.text.toString().trim()
        if (licencia.isEmpty()){
            et_licencia_frp.error="Esta vacio!"
            et_licencia_frp.requestFocus()
            return false
        }
        if (dni.isEmpty()){
            et_dni_frp.error="Dni esta vacio!"
            et_dni_frp.requestFocus()
            return false
        }
        if (nombre.isEmpty()){
            et_nombre_frp.error="Nombre esta vacio!"
            et_nombre_frp.requestFocus()
            return false
        }
        if (apellidos.isEmpty()){
            et_apellidos_frp.error="Apellido esta vacio!"
            et_apellidos_frp.requestFocus()
            return false
        }
        if (direccion.isEmpty()){
            et_direccion_frp.error="Direccion esta vacio!"
            et_direccion_frp.requestFocus()
            return false
        }
        if (number0.isEmpty()){
            et_telefono_frp.error="Telefono esta vacio!"
            et_telefono_frp.requestFocus()
            return false
        }

        if (dni.length!=8){
            et_dni_frp.error="El dni debe tener 8 digitos"
            et_dni_frp.requestFocus()
            return false
        }
        return true
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
                            et_photo_frp.setImageURI(value)
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
                            et_photo_frp.setImageURI(value)
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
        viewModel.updatePhotoLicencia(file, "userLicence", id).observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    login_progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    et_photo_frp.setImageURI(Uri.fromFile(file))
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
