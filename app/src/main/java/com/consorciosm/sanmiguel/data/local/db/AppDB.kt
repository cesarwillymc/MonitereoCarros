package com.consorciosm.sanmiguel.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.consorciosm.sanmiguel.common.constans.Constants
import com.consorciosm.sanmiguel.data.local.dao.UsuarioDao
import com.consorciosm.sanmiguel.data.model.Usuario

@Database(entities = [Usuario::class],version = 4)
abstract class AppDB:RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    companion object{
        @Volatile
        private var INSTANCE:AppDB?=null
        private val LOCK= Any()
        operator fun invoke(context: Context)= INSTANCE?: synchronized(LOCK){
            INSTANCE?:buildDatabase(context)
        }
        private fun buildDatabase(context: Context)= Room.databaseBuilder(context,AppDB::class.java, Constants.NAME_DATABASE)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()


    }
}