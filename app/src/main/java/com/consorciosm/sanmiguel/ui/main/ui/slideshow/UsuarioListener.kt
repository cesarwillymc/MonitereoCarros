package com.consorciosm.sanmiguel.UI.adapters

import com.consorciosm.sanmiguel.data.model.Usuario

interface UsuarioListener {
    fun onUsuarioClicked(usuario: Usuario, position: Int)
}