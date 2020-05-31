package com.consorciosm.sanmiguel.ui.main.admin.ui.registro_personal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseFragment
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.UsuarioList
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import com.consorciosm.sanmiguel.ui.main.admin.ui.registro_carros.SpinnerAdapter
import com.consorciosm.sanmiguel.ui.main.admin.ui.registro_carros.SpinnerListener
import kotlinx.android.synthetic.main.fragment_conductores.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


/**
 * A simple [Fragment] subclass.
 */
class Conductores : BaseFragment(), UsuarioListener, KodeinAware,SpinnerListener {
    var value: Boolean? = null
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    lateinit var usuarioAdapter: UsuarioAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = requireActivity().run {
            ViewModelProvider(this, factory).get(ViewModelMain::class.java)
        }

        val colors = listOf("all", "Sin Vehiculo", "Con Vehiculo")
        val datos = SpinnerAdapter(this,
            requireContext()
        )
        datos.updateData(colors)
        spinner.apply {
            dropDownHorizontalOffset
            isNestedScrollingEnabled=true
            adapter = datos
        }

        usuarioAdapter = UsuarioAdapter(this)
        fconductores_rv.apply {
            //, LinearLayoutManager.VERTICAL,false
            layoutManager = LinearLayoutManager(view.context)
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            adapter = usuarioAdapter
        }
        updateValues()
        swiperefresh.setOnRefreshListener {
            updateValues()
            swiperefresh.isRefreshing = false

        }
        spinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                value = when (position) {
                    0 -> {
                        null
                    }
                    1 -> {
                        false
                    }
                    else -> true
                }
                updateValues()
            }

        }
        add_user.setOnClickListener {
            val nav = ConductoresDirections.actionNavRegistroToRegistroCarro(true, "usuario._id")
            findNavController().navigate(nav)
        }
    }

    private fun updateValues() {
        viewModel.getListUser(value).observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    progressbar.visibility = View.GONE
                    usuarioAdapter.updateData(it.data)
                }
                is Resource.Failure -> {
                    snakBar(it.exception.message!!)
                    progressbar.visibility = View.GONE
                }
            }

        })
    }

    override fun getLayout(): Int = R.layout.fragment_conductores
    override fun onUsuarioClicked(usuario: UsuarioList, position: Int) {
        val nav = ConductoresDirections.actionNavRegistroToRegistroCarro(false, usuario._id)
        findNavController().navigate(nav)
    }

    override fun onCategoriaListener(dato: String, position: Int) {
        value = when (position) {
            0 -> {
                null
            }
            1 -> {
                false
            }
            else -> true
        }
        updateValues()
    }

}
