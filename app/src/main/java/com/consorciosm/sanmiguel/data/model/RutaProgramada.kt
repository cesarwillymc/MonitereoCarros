package com.consorciosm.sanmiguel.data.model

data class RutaProgramada(
    val recorrido:List<Puntos>,
    val kilometros: String,
    val nombres: String,
    val placa:String,
    val celular:String,
    val color: String,
    val inicioRecorrido: Puntos=Puntos(-15.8222872,-70.0151817),
    val finalRecorrido:Puntos= Puntos(-15.8232872,-70.0161817)
)
data class Puntos(
    val latitude:Double,
    val longitude:Double
)