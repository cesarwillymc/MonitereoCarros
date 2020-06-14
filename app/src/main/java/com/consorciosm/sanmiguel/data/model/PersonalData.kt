package com.consorciosm.sanmiguel.data.model

data class PersonalData(
    val dni:String,
    val nombres:String,
    val direccion:String,
    val apellidos:String,
    val telefono:String,
    val telefonoReferenciaA:String="",
    val telefonoReferenciaB:String="",
    val licencia:String="",
    val imgLicencia:String="",

    val password:String=""
)