package com.consorciosm.sanmiguel.data.network.repository

import com.consorciosm.sanmiguel.common.utils.SafeApiRequest
import com.consorciosm.sanmiguel.data.local.db.AppDB
import com.consorciosm.sanmiguel.data.model.Usuario
import com.consorciosm.sanmiguel.data.network.retrofit.ApiRetrofitKey
import com.google.firebase.firestore.FirebaseFirestore

class MainRepository (
    private val db: AppDB,
    private val api: ApiRetrofitKey,
    private val firebase: FirebaseFirestore
): SafeApiRequest() {
    fun deleteUser()= db.usuarioDao().deleteUsuario()
    fun updateUserAppDb(perfilUsuario: Usuario)=db.usuarioDao().updateUsuario(perfilUsuario)
    fun getUser()=db.usuarioDao().selectUsuario()
}