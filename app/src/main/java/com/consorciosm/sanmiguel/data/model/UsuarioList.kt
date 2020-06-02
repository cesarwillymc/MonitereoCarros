package com.consorciosm.sanmiguel.data.model

data class UsuarioList (
    val _id:String,
    val dni:String,
    val nombres:String,
    val apellidos:String,
    val telefono:String
)




data class ConductoresSinOrdenes (
    val _idConductor:String,
    val _idVehiculo:String,
    val nombres:String,
    val apellidos:String,
    val placa:String
)

