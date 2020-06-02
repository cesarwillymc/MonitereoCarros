package com.consorciosm.sanmiguel.ui.main.admin.ui.ordenes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.data.model.NotificacionesList
import kotlinx.android.synthetic.main.fragment_ordenes_programadas_item.view.*
import org.jetbrains.anko.backgroundColor

class NotificacionAdapter(val partesListener: NotificacionesListener): RecyclerView.Adapter<NotificacionAdapter.ViewHolder>() {

    var notificacionesList = ArrayList<NotificacionesList>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_ordenes_programadas_item, parent, false
            )
        )

    override fun getItemCount() = notificacionesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = notificacionesList[position]

        holder.itemView.lbl_destino_fop_item.text = user.camionetaPlaca
        holder.itemView.lbl_fecha_fop_item.text = user.fechaSolicitud
        holder.itemView.lbl_nombre_conductor_fop_item.text = user.conductorAutorizado
        if (user.approved){
            holder.itemView.estadoOrden.text = "VALIDADO"
            holder.itemView.estadoOrden.backgroundColor = holder.itemView.context.getColor(R.color.verde)
        }else{
            holder.itemView.estadoOrden.text = "NO VALIDADO"
            holder.itemView.estadoOrden.backgroundColor = holder.itemView.context.getColor(R.color.rojo)
        }


        holder.itemView.setOnClickListener {
            partesListener.listener(user,position)
        }

    }
    fun updateData(data: List<NotificacionesList>){
        notificacionesList.clear()
        notificacionesList.addAll(data)
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){




    }

}