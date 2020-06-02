package com.consorciosm.sanmiguel.data.model

data class OrdenProgramada(
    val aprobado: String,
    val autorizadoA: String,
    val autorizadoACargo: String,
    val autorizadoPor: String="",
    val autorizadoPorCargo: String="",
    val camionetaPlaca: String,
    val codigo: String,
    val conductorAutorizado: String,
    val fechaSolicitud: String,
    val formulario: String,
    val observaciones: String,
    val ocupantes: String,
    val proyecto: String,
    val retornoDestino: String,
    val retornoFecha: String,
    val retornoHora: String,
    val retornoOrigen: String,
    val salidaDestino: String,
    val salidaFecha: String,
    val salidaHora: String,
    val salidaOrigen: String,
    val tiempoEstimadoViaje: String,
    val version: String,
    val carId:String
)