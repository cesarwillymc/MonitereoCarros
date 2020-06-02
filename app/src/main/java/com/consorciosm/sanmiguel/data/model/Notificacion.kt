package com.consorciosm.sanmiguel.data.model

data class Notificacion(
    val _idDestinatario:String,
    val nombre:String,
    val remitenteid:String,
    val mensaje:String,
    val asunto:String
)