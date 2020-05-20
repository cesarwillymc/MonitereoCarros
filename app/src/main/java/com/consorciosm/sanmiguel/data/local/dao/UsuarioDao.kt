package com.consorciosm.sanmiguel.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.consorciosm.sanmiguel.common.constans.Constants
import com.consorciosm.sanmiguel.data.model.Usuario
import org.jetbrains.anko.db.ConflictClause

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsuario(usuario: Usuario)
    @Update
    fun updateUsuario(usuario: Usuario)
    @Query("DELETE FROM ${Constants.NAME_TABLE_USER}")
    fun deleteUsuario()
    @Query("SELECT * FROM ${Constants.NAME_TABLE_USER}" )
    fun selectUsuario():LiveData<Usuario>
}