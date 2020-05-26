package com.consorciosm.sanmiguel.data.network.repository

import android.util.Log
import com.consorciosm.sanmiguel.common.constans.Constants.PREF_ID_USER
import com.consorciosm.sanmiguel.common.shared.SharedPreferencsManager.Companion.setSomeStringValue
import com.consorciosm.sanmiguel.common.utils.SafeApiRequest
import com.consorciosm.sanmiguel.data.local.db.AppDB
import com.consorciosm.sanmiguel.data.model.ResponseGeneral
import com.consorciosm.sanmiguel.data.model.Usuario
import com.consorciosm.sanmiguel.data.model.requestSignIn
import com.consorciosm.sanmiguel.data.model.requestSignUp
import com.consorciosm.sanmiguel.data.network.retrofit.ApiRetrofitKey
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthRepository(
    private val db: AppDB,
    private val api:ApiRetrofitKey,
    private val firebase:FirebaseFirestore
):SafeApiRequest() {
    fun deleteUser()= db.usuarioDao().deleteUsuario()
    suspend fun saveUser(perfilUsuario: Usuario)= db.usuarioDao().insertUsuario(perfilUsuario)
    fun getUser()=db.usuarioDao().selectUsuario()
    suspend  fun SignUpAuth(model: requestSignUp): ResponseGeneral {
        return apiRequest { api.createUser(model) }
    }
    suspend fun SignInAuth(email: String, pass: String):requestSignIn{
        return apiRequest { api.loginUser(requestSignIn(email,pass)) }
    }

    suspend fun GetInformationUser():Usuario {
        val dato = apiRequest { api.getUserInfo()  }
        setSomeStringValue(PREF_ID_USER,dato._id)
        return dato
    }
}