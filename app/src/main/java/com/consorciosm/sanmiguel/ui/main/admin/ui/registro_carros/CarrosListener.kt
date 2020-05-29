package com.consorciosm.sanmiguel.ui.main.admin.ui.registro_carros

import com.consorciosm.sanmiguel.data.model.CarrosList

interface CarrosListener {
    fun listener(carrosList: CarrosList, position:Int)
}