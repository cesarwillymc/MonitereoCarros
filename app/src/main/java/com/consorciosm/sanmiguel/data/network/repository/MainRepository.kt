package com.consorciosm.sanmiguel.data.network.repository

import android.util.Log
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.common.utils.SafeApiRequest
import com.consorciosm.sanmiguel.data.local.db.AppDB
import com.consorciosm.sanmiguel.data.model.*
import com.consorciosm.sanmiguel.data.network.retrofit.ApiRetrofitKey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendAtomicCancellableCoroutine
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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
    suspend fun getRecorridoChoferId(value:String)= apiRequest { api.getRecorridoChofer(value) }
//    suspend fun getStringPoline( url:String) = apiRequest { api.callMaps(url) }.routes
    suspend fun getPositionsConductores(): Flow<Resource<List<PuntosFirebase>>> = callbackFlow {
        val conexion=firebase.collection("vehiculos").whereEqualTo("state",true).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException==null){
                val dato=querySnapshot!!.toObjects(PuntosFirebase::class.java)

                offer(Resource.Success(dato))
            }else{
                channel.close(Exception("Error al traer datos, revisa tu conexion"))
            }
        }
        awaitClose { conexion.remove() }
    }
}