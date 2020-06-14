package com.consorciosm.sanmiguel.ui.main.admin.ui.notify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.data.model.NotificacionesList
import kotlinx.android.synthetic.main.fragment_notificaciones_item.view.*
import org.jetbrains.anko.backgroundColor

class NotificacionAdapter(val partesListener: NotificacionesListener): RecyclerView.Adapter<NotificacionAdapter.ViewHolder>() {

    var notificacionesList = ArrayList<NotificacionesList>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_notificaciones_item,parent,false))

    override fun getItemCount() = notificacionesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = notificacionesList[position]

        holder.itemView.fni_txt_asunto.text = user.asunto
        holder.itemView.fni_txt_cuerpo.text = user.mensaje
        if (user.isOrder){
            holder.itemView.fni_txt_asunto.backgroundColor = holder.itemView.context.getColor(R.color.rojo)
        }else{
            holder.itemView.fni_txt_asunto.backgroundColor = holder.itemView.context.getColor(R.color.verde)

        }


        holder.itemView.setOnClickListener {
            partesListener.listener(user,position)
        }

    }

    fun updateData(data: List<NotificacionesList>){
        notificacionesList.addAll(data)
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){




    }

}