package com.consorciosm.sanmiguel.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.common.utils.detectar_formato
import com.consorciosm.sanmiguel.data.model.*
import com.consorciosm.sanmiguel.data.network.repository.MainRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ViewModelMain(private val repo: MainRepository) :ViewModel(){
    val getLoggetUser = repo.getUser()
    fun deleteUser()= repo.deleteUser()
    fun updateUserDB(item: Usuario)=repo.updateUserAppDb(item)
    fun createPersonal(personal:PersonalData):LiveData<Resource<Unit>> = liveData {
        emit(Resource.Loading())
        try{
            repo.createPersonal(personal)
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
            repo.updatePhotoRest(archivo!!,nameFile,dato.message)
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
    val conductoresSinVehiculo:LiveData<Resource<List<UsuarioList>>>  = liveData {
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
}