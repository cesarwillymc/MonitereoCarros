package com.consorciosm.sanmiguel.ui.main.admin.ui.partes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.data.model.PartesList
import kotlinx.android.synthetic.main.fragment_partes_item.view.*
import org.jetbrains.anko.backgroundColor

class PartesAdapter(val partesListener: PartesListener): RecyclerView.Adapter<PartesAdapter.ViewHolder>() {

    var partesList = ArrayList<PartesList>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
        R.layout.fragment_partes_item,parent,false))

    override fun getItemCount() = partesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = partesList[position]

        holder.itemView.lbl_conductor_item_parte.text = user.nombres
        holder.itemView.lbl_placa_item_parte.text = user.placa
        holder.itemView.lbl_model_item_parte.text = user.modelo
        holder.itemView.lbl_model_item_parte.backgroundColor= user.color
        if (user.accept){
            holder.itemView.estadoParte.backgroundColor = holder.itemView.context.getColor(R.color.verde)
            holder.itemView.estadoParte.text = "Aceptado"
        }else{
            holder.itemView.estadoParte.backgroundColor = holder.itemView.context.getColor(R.color.rojo)
            holder.itemView.estadoParte.text = "No Aceptado"
        }


        holder.itemView.setOnClickListener {
            partesListener.listener(user,position)
        }

    }
    fun updateData(data: List<PartesList>){
        partesList.clear()
        partesList.addAll(data)
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){




    }

}