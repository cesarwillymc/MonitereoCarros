package com.consorciosm.sanmiguel.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PuntosFirebase(
    var latitude:Double=0.toDouble(),
    var longitude:Double=0.toDouble(),
    var state:Boolean=false,
    var color:Int=-125556,
    var placa:String="",
    var id:String="",
    val idVehiculo:String=""
):Parcelable