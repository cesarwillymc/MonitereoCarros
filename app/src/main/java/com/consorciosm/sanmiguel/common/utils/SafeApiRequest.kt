package com.consorciosm.sanmiguel.common.utils

import android.util.Log
import com.consorciosm.sanmiguel.common.constans.Constants.PREF_TOKEN
import com.consorciosm.sanmiguel.common.shared.SharedPreferencsManager.Companion.setSomeStringValue
import com.consorciosm.sanmiguel.data.model.ErrorBody
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response


abstract class SafeApiRequest {
    suspend fun <T:Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful){
            try { if (response.headers().get("authToken")!=null){
                val token =  response.headers().get("authToken").toString()
                Log.e("token",token)
                setSomeStringValue(PREF_TOKEN,token)
            } }catch (e:Exception){ }
            return response.body()!!
        }else{
            val error= response.errorBody().toString()

            val message= StringBuilder()
            error?.let {
                 try {
                     val gson = Gson()
                     val type = object : TypeToken<ErrorBody>() {}.type
                     val errorResponse: ErrorBody? = gson.fromJson(response.errorBody()!!.charStream(), type)
                     Log.e("apisafe",errorResponse!!.message)
                    message.append(errorResponse.message)
                 }catch (e: Exception){ }
                message.append("\n")
            }
            message.append("Error code: ${response.code()}")
            throw Exception(message.toString())
        }
    }
}