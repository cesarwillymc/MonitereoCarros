package com.consorciosm.sanmiguel.data.model

data class ParteDiario(
    var fechaDia:String,
    var conductor:Usuario,
    var vehiculo:VehiculoInfo,
    var actividadDiaria:ActividadDiaria,
    var kilometraje:Kilometraje,
    var ncombustible:Ncombustible,
    var abastecimiento:Abastecimiento
)
data class EstadoVehiculo(
    var tarjetaPropiedad:String,
    var ConosSeguridad:String,
    var NivelAceite:String,
    var LiquidoFrenos:String,
    var Espejos:String,
    var Soat:String,
    var Botiquin:String,
    var NivelAgua:String,
    var LiquidoHidrolina:String,
    var GataPalanca:String,
    var Extintor:String,
    var LucesExteriores:String,
    var RefrigeranteRadiador:String,
    var AlarmaRetroceso:String,
    var Herramientas:String
)
data class ActividadDiaria(
    var actividad:String,
    var inicio:String,
    var termino:String
)
data class Kilometraje(
    var Kdiario:String,
    var Kinicio:String,
    var Ktermino:String
)
data class Ncombustible(
    var Ndecombustible:String,
    var photoNconbustible:String
)
data class Abastecimiento(
    var hora:String,
    var Kilometraje:String,
    var galones:String
)