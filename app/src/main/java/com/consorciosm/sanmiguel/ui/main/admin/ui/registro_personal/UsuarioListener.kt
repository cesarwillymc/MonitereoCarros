package com.consorciosm.sanmiguel.ui.main.admin.ui.registro_personal

import com.consorciosm.sanmiguel.data.model.Usuario
import com.consorciosm.sanmiguel.data.model.UsuarioList

interface UsuarioListener {
    fun onUsuarioClicked(usuario: UsuarioList, position: Int)
}