package com.consorciosm.sanmiguel.ui.main.admin.ui.registro_personal

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.data.model.UsuarioList
import kotlinx.android.synthetic.main.fragment_conductores_item.view.*

class UsuarioAdapter(val usuarioListener: UsuarioListener):RecyclerView.Adapter<UsuarioAdapter.ViewHolder>() {

    var listUsuarios = ArrayList<UsuarioList>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.fragment_conductores_item,parent,false))

    override fun getItemCount() = listUsuarios.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = listUsuarios[position]
        holder.itemView.fci_txt_celular.text = user.telefono
        holder.itemView.fci_txt_dni.text = user.dni
        holder.itemView.fci_txt_nombre.text = "${user.nombres} ${user.apellidos}"

        holder.itemView.setOnClickListener {
            usuarioListener.onUsuarioClicked(user,position)
        }

    }
    fun updateData(data: List<UsuarioList>){
        listUsuarios.clear()
        listUsuarios.addAll(data)
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

//        val lblNombre = itemView.findViewById<TextView>(R.id.lblNombreAlumnoAdmin)
//        val imgAlumno = itemView.findViewById<ImageView>(R.id.imgAlumnoAdmin)
//        val lblGrado = itemView.findViewById<TextView>(R.id.lblGradoAdmin)
//        val lblEscuela = itemView.findViewById<TextView>(R.id.lblEscuelaAddmin)


    }

}