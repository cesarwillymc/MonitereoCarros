package com.consorciosm.sanmiguel.data.network

import com.consorciosm.sanmiguel.data.local.db.AppDB
import com.consorciosm.sanmiguel.data.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository(
    private val db: AppDB,
    private val firebase:FirebaseFirestore
) {
    fun deleteUser()= db.usuarioDao().deleteUsuario()
    suspend fun saveUser(perfilUsuario: Usuario)= db.usuarioDao().insertUsuario(perfilUsuario)
    fun updateUserAppDb(perfilUsuario: Usuario)=db.usuarioDao().updateUsuario(perfilUsuario)
    fun getUser()=db.usuarioDao().selectUsuario()
}