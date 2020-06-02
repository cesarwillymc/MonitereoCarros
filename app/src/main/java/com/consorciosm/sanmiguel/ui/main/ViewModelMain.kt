package com.consorciosm.sanmiguel.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.common.utils.detectar_formato
import com.consorciosm.sanmiguel.data.model.*
import com.consorciosm.sanmiguel.data.network.repository.MainRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.HashMap

class ViewModelMain(private val repo: MainRepository) :ViewModel(){

    //Supervisor
    val obtenerNotificacionesCantidad= liveData {
        try{
            repo.obtenerNotificacionesCantidad().collect {
                emit(it)
            }
        }catch (e:Exception){
            Log.e("error",e.message)
        }
    }
    val getLoggetUser = repo.getUser()

    fun createOrdenProgramada(ordenProgramada:OrdenProgramada):LiveData<Resource<Unit>> = liveData {
        Log.e("Datos Enviados",ordenProgramada.toString())
        emit(Resource.Loading())
        try{
            repo.createOrdenProgramada(ordenProgramada)
            repo.IncrementValueNotification()
            emit(Resource.Success(Unit))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }

    fun createNotificacion(ordenProgramada:Notificacion):LiveData<Resource<Unit>> = liveData {
        Log.e("Datos Enviados",ordenProgramada.toString())
        emit(Resource.Loading())
        try{
            repo.createNotificacion(ordenProgramada)
            repo.IncrementValueNotificationConductor(ordenProgramada._idDestinatario)
            emit(Resource.Success(Unit))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun updaterdenProgramada(ordenProgramada:OrdenProgramada,id: String):LiveData<Resource<Unit>> = liveData {
        Log.e("Datosupdate",ordenProgramada.toString())
        emit(Resource.Loading())
        try{
            repo.updateOrdenProgramada(ordenProgramada,id)
            emit(Resource.Success(Unit))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }






    fun deleteUser()= repo.deleteUser()
    fun updateUserDB(item: Usuario)=repo.updateUserAppDb(item)
    fun createPersonal(personal:PersonalData):LiveData<Resource<Unit>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.createPersonal(personal)
            repo.CreateContadorNotifyUser(dato.message)
            emit(Resource.Success(Unit))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun updatePersonal(personal:PersonalData,id:String):LiveData<Resource<Unit>> = liveData {
        emit(Resource.Loading())
        try{
            repo.updatePersonal(personal,id)
            emit(Resource.Success(Unit))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun createVehiculo(vehiculo: VehiculoCreate,file: File,name:String):LiveData<Resource<Unit>> = liveData {
        emit(Resource.Loading())
        try{
            val nameFile = RequestBody.create(MediaType.parse("text/plain"),name )
            val archivo=guardarFotoEnArchivo(name,file)
            val dato=repo.createVehiculo(vehiculo)
            Log.e("cardid",dato.carId)
            repo.updatePhotoRest(archivo!!,nameFile,dato.carId)
            emit(Resource.Success(Unit))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun updatePhoto(file: File,name:String,id:String):LiveData<Resource<Unit>> = liveData {
        emit(Resource.Loading())
        try{
            val nameFile = RequestBody.create(MediaType.parse("text/plain"),name )
            val archivo=guardarFotoEnArchivo(name,file)
            repo.updatePhotoRest(archivo!!,nameFile,id)
            emit(Resource.Success(Unit))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun updateVehiculo(vehiculo: VehiculoCreate,id:String):LiveData<Resource<Unit>> = liveData {
        emit(Resource.Loading())
        try{
            repo.updateVehiculo(vehiculo,id)
            emit(Resource.Success(Unit))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    private fun guardarFotoEnArchivo(name: String, file: File): MultipartBody.Part? {
        var body: MultipartBody.Part? = null
        if (detectar_formato(file.path) == "ninguno") {
            throw Exception("Formato no valido de imagen")
        } else {
            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse("image/" + detectar_formato(file.path)),
                file
            )
            body = MultipartBody.Part.createFormData(name,file.name, requestFile)
        }
        return body
    }
    fun getListUser(value:Boolean?):LiveData<Resource<List<UsuarioList>>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.getListUser(value)
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun getListPartes(hora:String?=null,fecha:String):LiveData<Resource<List<PartesList>>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.getListPartes(hora,fecha)
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun getparteId(id:String):LiveData<Resource<Parte>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.getParteId(id)
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun validarParte(id:String):LiveData<Resource<Unit>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.validarParte(id)
            emit(Resource.Success(Unit))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun conductoresSinVehiculo():LiveData<Resource<List<UsuarioList>>>  = liveData (Dispatchers.IO){
        emit(Resource.Loading())
        try{
            val dato=repo.getListUser(false)
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun getListCarros(value:Boolean?):LiveData<Resource<List<CarrosList>>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.getListCarros(value)
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun getInfoUserById(id:String):LiveData<Resource<PersonalData>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.getInfoUser(id)
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun getInfoCarroById(id:String):LiveData<Resource<VehiculoCreate>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.getInfoCarro(id)
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }


    //Maps

    //Load Mapas Cars
    val markersTimeReal = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            repo.getPositionsConductores().collect {
                emit(it)
            }
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    fun getDataRecorridoConductor(id:String):LiveData<Resource<RutaProgramada>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.getRecorridoChoferId(id)
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun getListNotificaciones(pagina:Int):LiveData<Resource<List<NotificacionesList>>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.getListNotificaciones(pagina)
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun getListNotificacionesSupervisor(pagina:Int):LiveData<Resource<List<NotificacionesList>>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.getListNotificacionesSupervisor(pagina)
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }

    fun validateOrden(id:String):LiveData<Resource<ResponseGeneral>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.validateOrden(id)
            repo.IncrementValueNotificationConductor(dato.message)
            repo.DecrementValueNotification()
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun getNotificationById(id:String):LiveData<Resource<OrdenProgramada>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.getListNotificacionesById(id)
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    fun getNotificationByIdSupervisor(id:String):LiveData<Resource<OrdenProgramada>> = liveData {
        emit(Resource.Loading())
        try{
            val dato=repo.getNotificationByIdSupervisor(id)
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }
    //GetConfuctores With Placa
    fun ConductoresSinOrdenes():LiveData<Resource<List<ConductoresSinOrdenes>>>  = liveData (Dispatchers.IO){
        emit(Resource.Loading())
        try{
            val dato=repo.getConductoresSinOrdenes()
            emit(Resource.Success(dato))
        }catch (e:Exception){
            emit(Resource.Failure(e) )
        }
    }

//    @ExperimentalStdlibApi
//    fun getRutaMapa(origin: LatLng, dest: LatLng):LiveData<Resource<ArrayList<LatLng>>> = liveData(Dispatchers.IO){
//        emit(Resource.Loading())
//        try{
//            runBlocking {
//                val dato=repo.getStringPoline(getDirectionsUrl(origin,dest))
//                val convertData= getDataPoints(dato) as ArrayList<LatLng>
//                emit(Resource.Success(convertData))
//            }
//        }catch (e:Exception){
//            emit(Resource.Failure(e))
//        }
//    }

//    @ExperimentalStdlibApi
//    private suspend  fun getDataPoints(dato: MutableList<MutableList<HashMap<String, String>>>): List<LatLng> = buildList {
//        //var points: ArrayList<LatLng> = ArrayList()
//
//        for (i in dato) {
//            for (j in i) {
//                val lat = j["lat"]!!.toDouble()
//                val lng = j["lng"]!!.toDouble()
//                val position =
//                    LatLng(lat, lng)
//                add(position)
//            }
//        }
//    }
//
//    private fun getDirectionsUrl(
//        origin: LatLng,
//        dest: LatLng
//    ): String { // Punto de origen
//        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
//        // punto de destino
//        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
//        // Sensor de modo drive
//        val sensor = "sensor=false"
//        val mode = "mode=driving"
//        // Sensor
//        val parameters = "$str_origin&$str_dest&$sensor&$mode"
//        // Formato de salida
//        val output = "json"
//        // url
//        // https://maps.googleapis.com/maps/api/directions/json?origin=-15.837974456285096,-70.02117622643709&destination=-15.837974456285096,-70.02117622643709&sensor=false&mode=driving&key=AIzaSyD7kwgqDzGW8voiXP7gAbxaKnGY_Fr4Cng
//        return "$output?$parameters&key=AIzaSyBXQBe1pHpqQkclaoMEuAnZ6QVFbC860Yo"
//    }
}