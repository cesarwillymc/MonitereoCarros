package com.consorciosm.sanmiguel.data.network.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.consorciosm.sanmiguel.app.MyApp.Companion.getContextApp
import com.consorciosm.sanmiguel.common.constans.Constants
import com.consorciosm.sanmiguel.common.shared.SharedPreferencsManager.Companion.getSomeStringValue
import com.consorciosm.sanmiguel.common.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
class InterceptorToken : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable())
            throw NoInternetException("No cuenta con Internet, Revise su conexion")
        val request: Request =
            chain.request().newBuilder().addHeader("Authorization", getSomeStringValue(Constants.PREF_TOKEN)!!).build()
        return chain.proceed(request)
    }


    private fun isInternetAvailable(): Boolean {
        var result= false
        val connectivityManager= getContextApp().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                result= when {
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN)->true
                    else->false
                }
            }
        }
        return result
    }


}