package com.consorciosm.sanmiguel.data.model

data class ParteDiario(
    var infoGeneral: InfoGeneral,
    var estadoVehiculo: EstadoVehiculo,
    var actividadDiaria:ActividadDiaria,
    var ncombustible:Ncombustible,
    var abastecimiento:Abastecimiento,
    var vehiculoInfo: VehiculoInfo,
    var chofer: PersonalData
)
data class InfoGeneral(
    var fechaDia:String,
    var empresaProveedora:String,
    var licenciaEmpresa:String,
    var salidaGaraje:String,
    var entradaGaraje:String
)
data class EstadoVehiculo(
    var tarjetaPropiedad:Boolean,
    var ConosSeguridad:Boolean,
    var NivelAceite:Boolean,
    var LiquidoFrenos:Boolean,
    var Espejos:Boolean,
    var Soat:Boolean,
    var Botiquin:Boolean,
    var NivelAgua:Boolean,
    var LiquidoHidrolina:Boolean,
    var GataPalanca:Boolean,
    var Extintor:Boolean,
    var LucesExteriores:Boolean,
    var RefrigeranteRadiador:Boolean,
    var AlarmaRetroceso:Boolean,
    var Herramientas:Boolean
)
data class ActividadDiaria(
    var actividad:String,
    var Horainicio:String,
    var Kilometrajeinicio:String,
    var HoraFin:String,
    var KilometrajeFin:String
)
data class Ncombustible(
    var Ndecombustible:String,
    var imgCombustible:String
)
data class Abastecimiento(
    var Kilometraje:String,
    var galones:String
)