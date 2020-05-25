package com.consorciosm.sanmiguel.ui.auth

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.consorciosm.sanmiguel.common.constans.Constants.PREF_TOKEN
import com.consorciosm.sanmiguel.common.shared.SharedPreferencsManager.Companion.setSomeStringValue
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.Usuario
import com.consorciosm.sanmiguel.data.model.requestSignUp
import com.consorciosm.sanmiguel.data.network.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class AuthViewModel(private val repo: AuthRepository) :ViewModel(){

    val getLoggetUser = repo.getUser()
    fun deleteUser()= repo.deleteUser()
    fun updateUserDB(item:Usuario)=repo.updateUserAppDb(item)

    fun SignIn(email: String,pass: String): LiveData<Resource<Unit>> = liveData(Dispatchers.IO){
        emit(Resource.Loading())
        try{
            repo.SignInAuth(email, pass)
            emit(Resource.Success(Unit))

        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    fun SignUp(usuario: requestSignUp):LiveData<Resource<Unit>> = liveData(Dispatchers.IO){
        emit(Resource.Loading())
        try{
            repo.SignUpAuth(usuario)
            emit(Resource.Success(Unit))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }

    fun IsValidNumber(phone:String):Boolean= phone.length>=9
    fun IsValidNumberDoc(dni:String):Boolean= dni.length==8

    fun IsValidEmail(email:String):Boolean =  Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun IsValidPass(pass:String):Boolean= pass.length>=6

}