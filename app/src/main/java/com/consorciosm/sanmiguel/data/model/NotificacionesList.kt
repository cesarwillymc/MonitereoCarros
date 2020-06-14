package com.consorciosm.sanmiguel.data.model

data class NotificacionesList(
    val approved:Boolean,
    val _id:String,
    val conductorAutorizado:String,
    val fechaSolicitud:String,
    val camionetaPlaca:String,
    val isOrder:Boolean,
    val isAprobed:Boolean,
    val asunto:String,
    val mensaje:String
)