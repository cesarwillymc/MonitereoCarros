package com.consorciosm.sanmiguel.ui.main

import androidx.lifecycle.ViewModel
import com.consorciosm.sanmiguel.data.model.Usuario
import com.consorciosm.sanmiguel.data.network.repository.MainRepository

class ViewModelMain(private val repo: MainRepository) :ViewModel(){
    val getLoggetUser = repo.getUser()
    fun deleteUser()= repo.deleteUser()
    fun updateUserDB(item: Usuario)=repo.updateUserAppDb(item)
}