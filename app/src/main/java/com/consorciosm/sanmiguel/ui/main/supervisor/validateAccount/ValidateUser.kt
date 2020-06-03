package com.consorciosm.sanmiguel.ui.main.supervisor.validateAccount

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseActivity
import com.consorciosm.sanmiguel.common.shared.SharedPreferencsManager
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.ui.auth.view.LoginActivity
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.fragment_validate_user.*
import org.jetbrains.anko.contentView
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ValidateUser : BaseActivity(),KodeinAware {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    override fun getLayout(): Int = R.layout.fragment_validate_user
    lateinit var adapterV:ValidateAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        adapterV=ValidateAdapter(viewModel,contentView!!)
        validate_user_rv.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            adapter = adapterV
        }
        viewModel.getValidateAdmin().observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    register_progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    Log.e("datos",it.data.toString())
                    register_progressbar.visibility = View.GONE
                    adapterV.updateData(it.data)
                }
                is Resource.Failure -> {
                    snakBar(it.exception.message!!)
                    register_progressbar.visibility = View.GONE
                }
            }
        })
        logout_validate_user.setOnClickListener {
            viewModel.deleteUser()
            SharedPreferencsManager.clearAllManagerShared()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
