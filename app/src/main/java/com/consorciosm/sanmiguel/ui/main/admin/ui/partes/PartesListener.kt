package com.consorciosm.sanmiguel.ui.main.admin.ui.partes

import com.consorciosm.sanmiguel.data.model.PartesList

interface PartesListener {
    fun listener(partesList: PartesList, position:Int)
}