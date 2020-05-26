package com.consorciosm.sanmiguel.UI.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.data.model.Usuario

class UsuarioAdapter(val usuarioListener: UsuarioListener):RecyclerView.Adapter<UsuarioAdapter.ViewHolder>() {

    var listUsuarios = ArrayList<Usuario>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.fragment_registro_carros,parent,false))

    override fun getItemCount() = listUsuarios.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = listUsuarios[position] as Usuario
//
//        holder.lblNombre.text = user.name
//        holder.lblEscuela.text = user.school
//        holder.lblGrado.text = user.grado
//        FirebaseStorage.getInstance().getReference("imagenes/perfil/${user.id.replace("-","")}.jpg").downloadUrl.addOnSuccessListener(
//            OnSuccessListener<Any> { uri ->
//                Glide.with(holder.itemView.context).load(uri.toString()).into(holder.imgAlumno)
//            })
        holder.itemView.setOnClickListener {
            usuarioListener.onUsuarioClicked(user,position)
        }

    }
    fun updateData(data: List<Usuario>){
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