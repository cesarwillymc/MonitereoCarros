package com.consorciosm.sanmiguel.data.model

data class RutaProgramada(
    val recorrido:List<Puntos>,
    val kilometros: String,
    val nombres: String,
    val placa:String,
    val celular:String,
    val color: String,
    val inicioRecorrido: Puntos,
    val finalRecorrido:Puntos
)
data class Puntos(
    val latitude:Double,
    val longitude:Double
)