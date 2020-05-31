package com.consorciosm.sanmiguel.data.model

data class ParteDiario(
    var infoGeneral: InfoGeneral,
    var estadoVehiculo: EstadoVehiculo,
    var actividadDiaria:ActividadDiaria,
    var ncombustible:Ncombustible,
    var abastecimiento:Abastecimiento
)
data class InfoGeneral(
    var fechaDia:String,
    var empresaProveedora:String,
    var licenciaEmpresa:String,
    var salidaGaraje:String,
    var entradaGaraje:String
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
    var Horainicio:String,
    var Kilometrajeinicio:String,
    var HoraFin:String,
    var KilometrajeFin:String
)
data class Ncombustible(
    var Ndecombustible:String
)
data class Abastecimiento(
    var Kilometraje:String,
    var galones:String
)