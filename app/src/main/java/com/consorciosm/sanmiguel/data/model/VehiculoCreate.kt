package com.consorciosm.sanmiguel.data.model

data class VehiculoCreate(
    val numeroPlaca:String,
    val conductor:String,
    val numeroSerie:String,
    val numeroVin:String,
    val numeroMotor:String,
    val color:String,
    val marca:String,
    val modelo:String,
    val placaVigente:String,
    val placaAnterior:String="",
    val estado:String,
    val anotaciones:String,
    val sede:String,
    val kilometros:String,
    val nivelCombustible:String,
    val imagenCarro:String="",
    val nombres:String="",
    val apellidos:String=""
)

