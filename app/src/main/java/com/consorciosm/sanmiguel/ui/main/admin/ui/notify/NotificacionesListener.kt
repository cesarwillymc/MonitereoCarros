package com.consorciosm.sanmiguel.ui.main.admin.ui.notify

import com.consorciosm.sanmiguel.data.model.NotificacionesList


interface NotificacionesListener {
    fun listener(notificacionesList: NotificacionesList, position:Int)
}