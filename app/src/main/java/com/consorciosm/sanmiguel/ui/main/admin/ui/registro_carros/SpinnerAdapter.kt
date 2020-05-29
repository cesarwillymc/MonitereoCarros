package com.consorciosm.sanmiguel.ui.main.admin.ui.registro_carros

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.consorciosm.sanmiguel.R

@SuppressLint("ViewHolder")
class SpinnerAdapter(private val spinnerListener: SpinnerListener, private val context:Context): BaseAdapter() {
    var inflater: LayoutInflater = LayoutInflater.from(context)
    var lisProducts:List<String>? = null
//    listOf(ProductPrice("Ingresa datos ","",""))
    fun updateData(data: List<String>?){
        Log.e("adapter",data.toString())

        lisProducts=data
        notifyDataSetChanged()
    }
    fun getData():List<String>?{
        return lisProducts
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getCustomView(position,convertView,parent)
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
            return 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getCustomView(position,convertView,parent)
    }
    override fun getCount(): Int = if (lisProducts!=null) lisProducts!!.size else 0
    fun getCustomView(position: Int, convertView: View?, parent: ViewGroup?):View{
        val view =inflater.inflate( R.layout.item_spinner,parent,false)
        val direccionPedido = view.findViewById<TextView>(R.id.spinnerValue)
        direccionPedido.text=lisProducts?.get(position)
        view.setOnClickListener {
            spinnerListener.onCategoriaListener(lisProducts?.get(position)!!,position)
        }
        return view
    }

}