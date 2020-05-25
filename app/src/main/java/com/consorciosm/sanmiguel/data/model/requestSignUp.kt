package com.consorciosm.sanmiguel.data.model

data class requestSignUp (
    var nombres:String?=null,
    var paterno:String?=null,
    var materno:String?=null,
    var dni:String?=null,
    var telefono:String?=null,
    var direccion:String?=null,
    var password:String=""
)