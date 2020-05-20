package com.consorciosm.sanmiguel.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.consorciosm.sanmiguel.data.model.Usuario
import com.consorciosm.sanmiguel.data.network.AuthRepository

class AuthViewModel(private val repo: AuthRepository) :ViewModel(){

    fun getLoggetUser() = repo.getUser()
    fun deleteUser()= repo.deleteUser()
    fun updateUserDB(item:Usuario)=repo.updateUserAppDb(item)


    fun IsValidNumber(phone:String):Boolean= phone.length>=9
    fun IsValidNumberDoc(dni:String):Boolean= dni.length==8

    fun IsValidEmail(email:String):Boolean =  Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun IsValidPass(pass:String):Boolean= pass.length>=6

}