package com.consorciosm.sanmiguel.data.network.retrofit

import com.consorciosm.sanmiguel.common.constans.Constants.BASE_URL_API
import com.consorciosm.sanmiguel.data.model.*
import com.consorciosm.sanmiguel.ui.main.supervisor.validateAccount.ValidateUsuarios
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiRetrofitKey {



    @POST("admin/orden")
    suspend fun createOrdenProgramada(
        @Body orden: OrdenProgramada
    ): Response<ResponseGeneral>
    @POST("admin/sendMenssage/{id}")
    suspend fun createNotificacion(
        @Body orden: Notificacion,
        @Path("id") id :String
    ): Response<ResponseGeneral>
    @POST("supervisor/sendMenssage/{id}")
    suspend fun createNotificacionSupervisor(
        @Body orden: Notificacion,
        @Path("id") id :String
    ): Response<ResponseGeneral>



    @PUT("admin/orden/{id}")
    suspend fun updateOrdenProgramada(
        @Body orden: OrdenProgramada,
        @Path("id") id :String
    ): Response<ResponseGeneral>


    @POST("admin/registro")
    suspend fun createUser(
        @Body usuario: requestSignUp
    ): Response<ResponseGeneral>
    @POST("admin/login")
    suspend fun loginUser(
        @Body request: requestSignIn
    ): Response<requestSignIn>
    @GET("admin/informacion")
    suspend fun getUserInfo(): Response<Usuario>
    @POST("admin/crearUser")
    suspend fun createPersonal(
        @Body usuario: PersonalData
    ): Response<ResponseGeneral>
    @PUT("admin/updateUser/{id}")
    suspend fun updatePersonal(
        @Body usuario: PersonalData,
        @Path("id") id :String
    ): Response<ResponseGeneral>
    @POST("admin/createVehiculo")
    suspend fun createVehiculo(
        @Body vehiculo: VehiculoCreate
    ): Response<ResponseGeneral>
    @Multipart
    @PUT("admin/carImg/{carId}")
    suspend fun createPhotoVehiculo(
        @Part file: MultipartBody.Part,
        @Part("carImg") name: RequestBody,
        @Path("carId") carId:String?
    ): Response<ResponseGeneral>
    @Multipart
    @PUT("admin/userLicence/{carId}")
    suspend fun createPhotoLicenciaUser(
        @Part file: MultipartBody.Part,
        @Part("userLicence") name: RequestBody,
        @Path("carId") carId:String?
    ): Response<ResponseGeneral>
    @Multipart
    @PUT("service/setServiceImg")
    suspend fun updatePhotoVehiculo(
        @Part file: MultipartBody.Part,
        @Part("service") name: RequestBody,
        @Query("key") key:String?
    ): Response<ResponseGeneral>
    @PUT("admin/updateVehiculo/{id}")
    suspend fun updateVehiculo(
        @Body vehiculo: VehiculoCreate,
        @Path("id") id:String

    ): Response<ResponseGeneral>

    //GetCarros

    @GET("admin/cars")
    suspend fun getListCarros(
        @Query("chofer")chofer:Boolean?=null
    ):Response<List<CarrosList>>
    @GET("admin/carinfo/{id}")
    suspend fun getCarroId(
        @Path("id") id:String
    ):Response<VehiculoCreate>
    //GetUsuarios
    @GET("admin/users")
    suspend fun getListUser(
        @Query("vehiculo") vehiculo:Boolean?=null
    ):Response<List<UsuarioList>>
    @GET("supervisor/listAdmin")
    suspend fun getListAdmin(
    ):Response<List<UsuarioList>>
    @GET("admin/listChoferByPlaca")
    suspend fun getConductoresSinOrdenes(
    ):Response<List<ConductoresSinOrdenes>>
    @GET("admin/parteInfo")
    suspend fun OrdenesDefaultretrofit(
    ):Response<OrdenesDefault>
    @GET("admin/userinfo/{id}")
    suspend fun getUserId(
        @Path("id") id:String
    ):Response<PersonalData>


    @GET("admin/recorrido/{id}")
    suspend fun getRecorridoChofer(
        @Path("id") id: String,
        @Query("fecha")inicio: String,
        @Query("cantidad")final: String
    ):Response<RutaProgramada>
    //Get Partes
    @GET("admin/listParte")
    suspend fun getListPartes(
        @Query("fecha") fecha:String,
        @Query("hora") hora:String?=null
    ):Response<List<PartesList>>
    @GET("admin/userParte/{id}")
    suspend fun getParteId(
        @Path("id") id:String
    ):Response<Parte>
    @POST("admin/validaParte/{id}")
    suspend fun validarParte(
        @Path("id") id:String
    ):Response<ParteDiario>
//    @GET
//    suspend fun callMaps(
//        @Url url: String?
//    ): Response<ResponseMaps>
    @GET("admin/getLisOrdenes")
    suspend fun getListNotificaciones(
        @Query("pagina") pagina:Int
    ):Response<List<NotificacionesList>>
    @GET("admin/getListMensajes")
    suspend fun     getListNotificacionesSupervisorSMS(
        @Query("pagina") pagina:Int
    ):Response<List<NotificacionesList>>
    @GET("admin/orden/{id}")
    suspend fun getListNotificacionesById(
        @Path("id") id:String
    ):Response<OrdenProgramada>
    //Supervisor datos
    @GET("supervisor/listOrdenes")
    suspend fun getListNotificacionesSupervisor(
        @Query("pagina") pagina:Int
    ):Response<List<NotificacionesList>>
    @GET("supervisor/orden/{id}")
    suspend fun getNotificationByIdSupervisor(
        @Path("id") id:String
    ):Response<OrdenProgramada>
    @POST("supervisor/valideOrden/{id}")
    suspend fun validateOrden(
        @Path("id") id:String,
        @Query("isAproved") aproved:Boolean
    ):Response<ResponseGeneral>



    //SuperAdmin
    @GET("superadmin/sinRole")
    suspend fun getValidateAdmin():Response<List<ValidateUsuarios>>
    @FormUrlEncoded
    @PUT("superadmin/setRole/{id}")
    suspend fun updateUsuariosSuperAdmin(
        @Path("id") id:String,
        @Field("role") role:String
    ):Response<ResponseGeneral>
    companion object{
        operator fun invoke() : ApiRetrofitKey{
            val okHttpClienteBuilder= OkHttpClient.Builder().addInterceptor(InterceptorToken()).build()

            val cliente: OkHttpClient = okHttpClienteBuilder
            return   Retrofit.Builder()
                .baseUrl(BASE_URL_API)
                .client(cliente)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRetrofitKey::class.java)
        }

    }
}