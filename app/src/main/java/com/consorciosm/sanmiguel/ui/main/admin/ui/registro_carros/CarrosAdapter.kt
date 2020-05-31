package com.consorciosm.sanmiguel.ui.main.admin.ui.registro_carros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.data.model.CarrosList
import kotlinx.android.synthetic.main.fragment_carros_item.view.*
import org.jetbrains.anko.backgroundColor

class CarrosAdapter(val carrosListener: CarrosListener):RecyclerView.Adapter<CarrosAdapter.ViewHolder>() {

    var carrosList = ArrayList<CarrosList>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.fragment_carros_item,parent,false))

    override fun getItemCount() = carrosList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = carrosList[position]

        holder.itemView.fci_txt_placa.text = user.numeroPlaca
        holder.itemView.fci_txt_modelo.text = user.modelo
        holder.itemView.fci_txt_color.text = user.color
        holder.itemView.textView21.backgroundColor= user.color.toInt()


        holder.itemView.setOnClickListener {
            carrosListener.listener(user,position)
        }

    }
    fun updateData(data: List<CarrosList>){
        carrosList.clear()
        carrosList.addAll(data)
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

//        val lblNombre = itemView.findViewById<TextView>(R.id.lblNombreAlumnoAdmin)
//        val imgAlumno = itemView.findViewById<ImageView>(R.id.imgAlumnoAdmin)
//        val lblGrado = itemView.findViewById<TextView>(R.id.lblGradoAdmin)
//        val lblEscuela = itemView.findViewById<TextView>(R.id.lblEscuelaAddmin)


    }

}