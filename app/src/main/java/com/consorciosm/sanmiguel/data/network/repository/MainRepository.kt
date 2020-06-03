package com.consorciosm.sanmiguel.data.network.repository

import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.common.utils.SafeApiRequest
import com.consorciosm.sanmiguel.data.local.db.AppDB
import com.consorciosm.sanmiguel.data.model.*
import com.consorciosm.sanmiguel.data.network.retrofit.ApiRetrofitKey
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainRepository (
    private val db: AppDB,
    private val api: ApiRetrofitKey,
    private val firebase: FirebaseFirestore
): SafeApiRequest() {

    suspend fun createOrdenProgramada(ordenProgramada: OrdenProgramada)= apiRequest { api.createOrdenProgramada(ordenProgramada) }
    suspend fun createNotificacion(ordenProgramada: Notificacion)= apiRequest { api.createNotificacion(ordenProgramada,ordenProgramada._idDestinatario) }
    suspend fun updateOrdenProgramada(ordenProgramada: OrdenProgramada,id:String)= apiRequest { api.updateOrdenProgramada(ordenProgramada,id) }

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
    suspend fun getConductoresSinOrdenes()= apiRequest { api.getConductoresSinOrdenes() }
    suspend fun getListPartes(hora:String?=null,fecha:String)= apiRequest { api.getListPartes(fecha,hora) }
    suspend fun getParteId(id:String)= apiRequest { api.getParteId(id) }
    suspend fun validarParte(id:String)= apiRequest { api.validarParte(id) }
    suspend fun getListCarros(value:Boolean?)= apiRequest { api.getListCarros(value) }
    suspend fun getInfoCarro(value:String)= apiRequest { api.getCarroId(value) }
    suspend fun getInfoUser(value:String)= apiRequest { api.getUserId(value) }
    suspend fun getRecorridoChoferId(
        value: String,
        inicio: String,
        final: String
    )= apiRequest { api.getRecorridoChofer(value,inicio,final) }
//    suspend fun getStringPoline( url:String) = apiRequest { api.callMaps(url) }.routes
    suspend fun getListNotificaciones(pagina:Int)= apiRequest { api.getListNotificaciones(pagina) }
    suspend fun getListNotificacionesSupervisor(pagina:Int)= apiRequest { api.getListNotificacionesSupervisor(pagina) }

    suspend fun getListNotificacionesById(id:String)= apiRequest { api.getListNotificacionesById(id) }
    suspend fun getNotificationByIdSupervisor(id:String)= apiRequest { api.getNotificationByIdSupervisor(id) }
    suspend fun validateOrden(id:String)= apiRequest { api.validateOrden(id) }

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
    suspend fun obtenerNotificacionesCantidad(): Flow<Int> = callbackFlow {
        val conexion=firebase.collection("notify").document("supervisor").addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException==null){
                val dato=documentSnapshot!!.getLong("cantidad")!!.toInt()
                offer(dato)
            }else{
                channel.close(Exception("Error al traer datos, revisa tu conexion"))
            }
        }
        awaitClose { conexion.remove() }
    }
    suspend fun IncrementValueNotification(){
        firebase.collection("notify").document("supervisor").update("cantidad",FieldValue.increment(1))
    }
    suspend fun DecrementValueNotification(){
        firebase.collection("notify").document("supervisor").update("cantidad",FieldValue.increment(-1))
    }
    suspend fun IncrementValueNotificationConductor(id:String){
        firebase.collection("notify").document(id).update("cantidad",FieldValue.increment(1))
    }
    suspend fun CreateContadorNotifyUser(id: String){
        firebase.collection("notify").document(id).set(Varible(0))
    }
    suspend fun getValidateAdmin()= apiRequest { api.getValidateAdmin() }
    suspend fun updateUsuariosSuperAdmin(id:String,role:String)= apiRequest { api.updateUsuariosSuperAdmin(id,role) }
}
data class Varible(
    val cantidad:Int
)