package com.consorciosm.sanmiguel.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.consorciosm.sanmiguel.common.constans.Constants

@Entity(tableName = Constants.NAME_TABLE_USER)
data class Usuario(
    @PrimaryKey(autoGenerate = false)
    var _id:String,
    var registerDate:String,
    var role:String,
    var accountActive:Boolean,
    var dni:String,
    var apellidos:String,
    var nombres:String,
    var telefono:String,
    var direccion:String
)