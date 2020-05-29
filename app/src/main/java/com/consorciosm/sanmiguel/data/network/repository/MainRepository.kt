package com.consorciosm.sanmiguel.data.network.repository

import com.consorciosm.sanmiguel.common.utils.SafeApiRequest
import com.consorciosm.sanmiguel.data.local.db.AppDB
import com.consorciosm.sanmiguel.data.model.PersonalData
import com.consorciosm.sanmiguel.data.model.ResponseGeneral
import com.consorciosm.sanmiguel.data.model.Usuario
import com.consorciosm.sanmiguel.data.model.VehiculoCreate
import com.consorciosm.sanmiguel.data.network.retrofit.ApiRetrofitKey
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainRepository (
    private val db: AppDB,
    private val api: ApiRetrofitKey,
    private val firebase: FirebaseFirestore
): SafeApiRequest() {
    fun deleteUser()= db.usuarioDao().deleteUsuario()
    fun updateUserAppDb(perfilUsuario: Usuario)=db.usuarioDao().updateUsuario(perfilUsuario)
    fun getUser()=db.usuarioDao().selectUsuario()
    suspend fun createPersonal(personalData: PersonalData)= apiRequest { api.createPersonal(personalData) }
    suspend fun updatePersonal(personalData: PersonalData,id:String)= apiRequest { api.updatePersonal(personalData,id) }
    suspend fun createVehiculo(vehiculo: VehiculoCreate)= apiRequest { api.createVehiculo(vehiculo) }
    suspend fun updatePhotoRest(file: MultipartBody.Part, name: RequestBody, key:String?=null): ResponseGeneral {
        return apiRequest { api.createPhotoVehiculo(file,name,key) }
    }
    suspend fun updateVehiculo(vehiculo: VehiculoCreate,id:String)=apiRequest { api.updateVehiculo(vehiculo,id) }
    suspend fun getListUser(value:Boolean?)= apiRequest { api.getListUser(value) }
    suspend fun getListCarros(value:Boolean?)= apiRequest { api.getListCarros(value) }
    suspend fun getInfoCarro(value:String)= apiRequest { api.getCarroId(value) }
    suspend fun getInfoUser(value:String)= apiRequest { api.getUserId(value) }

}