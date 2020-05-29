package com.consorciosm.sanmiguel.data.model

data class PersonalData(
    val dni:String,
    val nombres:String,
    val direccion:String,
    val apellidos:String,
    val telefono:String,
    val telefonoReferenciaA:String="",
    val telefonoReferenciaB:String="",
    val password:String=""
)