package com.consorciosm.sanmiguel.ui.main.admin.ui.monitoreo

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.common.constans.Constants
import com.consorciosm.sanmiguel.common.utils.Resource
import com.consorciosm.sanmiguel.data.model.PuntosFirebase
import com.consorciosm.sanmiguel.ui.main.MainViewModelFactory
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import kotlinx.android.synthetic.main.dialog_detail_frag_monitoreo_personal.*
import org.jetbrains.anko.backgroundColor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class Dialog_info_conductor: DialogFragment(), KodeinAware {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: ViewModelMain
    private val factory: MainViewModelFactory by instance()
    lateinit var keyProduct:PuntosFirebase
    private lateinit var dialogPrincipal: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel= requireActivity().run {
            ViewModelProvider(this,factory).get(ViewModelMain::class.java)
        }
        return inflater.inflate(R.layout.dialog_detail_frag_monitoreo_personal, container, false)

    }
    override fun onStart() {
        super.onStart()
        dialogPrincipal.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keyProduct= Dialog_info_conductorArgs.fromBundle(requireArguments()).id
        Log.e("carroinfo",keyProduct.toString())
        viewModel.getInfoCarroById(keyProduct.idVehiculo).observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    login_progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    login_progressbar.visibility = View.GONE
                    Log.e("datos",it.data.toString())
                    lbl_conductor_dialog_detail.text= "${it.data.nombres} ${it.data.apellidos}"
                    lbl_placa_dialog_detail.text= it.data.numeroPlaca
                    lbl_color_dialog_detail.backgroundColor = it.data.color.toInt()
                    lbl_modelo_dialog_detail.text= "${it.data.marca} ${it.data.modelo}"
                    lbl_estado_dialog_detail.text= it.data.estado
                    Glide.with(requireContext()).load(Constants.BASE_URL_AMAZON_IMG+ it.data.imagenCarro).into(img_carro_dialog_detail)
                }
                is Resource.Failure -> {
                    login_progressbar.visibility = View.GONE
                    Toast.makeText(requireContext(),it.exception.message, Toast.LENGTH_LONG).show()
                }
            }
        })
        recorrido.setOnClickListener {
            val nav= Dialog_info_conductorDirections.actionDialogInfoConductorToMonitoreoPreview(keyProduct.id)
            findNavController().navigate(nav)
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        dialogPrincipal=super.onCreateDialog(savedInstanceState)
        return dialogPrincipal
    }
}