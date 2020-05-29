package com.consorciosm.sanmiguel.data.model

data class VehiculoCreate(
    val Nplaca:String,
    val Conductor:String,
    val Nserie:String,
    val Nvin:String,
    val Nmotor:String,
    val color:String,
    val Nmarca:String,
    val modelo:String,
    val placaVigente:String,
    val placaAnterior:String,
    val estado:String,
    val anotaciones:String,
    val sede:String,
    val kilometros:String,
    val nivelCombustible:String,
    val img:String=""
)