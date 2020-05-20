package com.consorciosm.sanmiguel.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.consorciosm.sanmiguel.common.constans.Constants

@Entity(tableName = Constants.NAME_TABLE_USER)
data class Usuario(
    @PrimaryKey
    var id:Int=0,
    var emailAddress:String,
    var mobilePhone:String
)