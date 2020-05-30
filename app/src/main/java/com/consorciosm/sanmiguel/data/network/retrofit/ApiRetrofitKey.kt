package com.consorciosm.sanmiguel.data.network.retrofit

import com.consorciosm.sanmiguel.common.constans.Constants.BASE_URL_API
import com.consorciosm.sanmiguel.data.model.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiRetrofitKey {

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
    @GET("admin/userinfo/{id}")
    suspend fun getUserId(
        @Path("id") id:String
    ):Response<PersonalData>

    @GET("admin/userinfo/{id}")
    suspend fun getRecorridoChofer(
        @Path("id") id:String
    ):Response<RutaProgramada>
//    @GET
//    suspend fun callMaps(
//        @Url url: String?
//    ): Response<ResponseMaps>
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