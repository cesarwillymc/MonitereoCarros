package com.consorciosm.sanmiguel.ui.main.supervisor.notificaciones

import com.consorciosm.sanmiguel.data.model.NotificacionesList

interface ValidacionListener {
    fun listener(notificacionesList: NotificacionesList, position:Int)
}