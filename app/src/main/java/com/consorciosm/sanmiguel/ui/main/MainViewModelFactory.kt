package com.consorciosm.sanmiguel.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.consorciosm.sanmiguel.data.network.repository.AuthRepository
import com.consorciosm.sanmiguel.data.network.repository.MainRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val repo: MainRepository) :ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModelMain(repo) as T
    }
}