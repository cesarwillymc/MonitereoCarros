package com.consorciosm.sanmiguel.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import com.bugsnag.android.Bugsnag
import com.consorciosm.sanmiguel.data.local.db.AppDB
import com.consorciosm.sanmiguel.data.network.repository.AuthRepository
import com.consorciosm.sanmiguel.data.network.repository.MainRepository
import com.consorciosm.sanmiguel.data.network.retrofit.ApiRetrofitKey
import com.consorciosm.sanmiguel.ui.auth.AuthViewModelFactory
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MyApp : Application(), LifecycleObserver, KodeinAware {

    override val kodein= Kodein.lazy {
        import(androidXModule(this@MyApp) )
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from singleton {
            AuthRepository(
                instance(),
                instance(),
                instance()
            )
        }
        bind() from provider { MainViewModelFactory(instance()) }
        bind() from singleton {
            MainRepository(
                instance(),
                instance(),
                instance()
            )
        }
        bind() from singleton { ApiRetrofitKey() }
        bind() from singleton { FirebaseFirestore.getInstance() }
        bind() from singleton { AppDB(instance()) }

    }
    companion object{
        private lateinit var instance:MyApp
        fun getInstanceApp():MyApp = instance
        fun getContextApp(): Context =instance
        fun setInstance(instance:MyApp){
            this.instance=instance
        }

    }
    override fun onCreate() {
        super.onCreate()
        setInstance(this)
        Bugsnag.start(this)
    }

}