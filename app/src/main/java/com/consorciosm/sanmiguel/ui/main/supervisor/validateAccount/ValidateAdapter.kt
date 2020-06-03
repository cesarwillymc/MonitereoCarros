package com.consorciosm.sanmiguel.ui.main.supervisor.validateAccount

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.common.constans.Constants
import com.consorciosm.sanmiguel.common.constans.Constants.ROLE_ADMINISTRADOR
import com.consorciosm.sanmiguel.common.constans.Constants.ROLE_SUPERVISOR
import com.consorciosm.sanmiguel.ui.main.ViewModelMain
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_validate_user_item.view.*


data class ValidateUsuarios(
    val _id:String,
    val name:String,
    val apellidos:String,
    val phone:String,
    val role:String
)
class ValidateAdapter(
    private val viewModel: ViewModelMain,
    private val contentView: View
) : RecyclerView.Adapter<ValidateAdapter.ViewHolder>() {

    var carrosList = ArrayList<ValidateUsuarios>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
        R.layout.fragment_validate_user_item,parent,false))

    override fun getItemCount() = carrosList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = carrosList[position]

        holder.itemView.lbl_usuario_txt.text = user.name +" "+ user.apellidos
        holder.itemView.lbl_celular_txt.text = user.phone
        when(user.role){
            Constants.ROLE_SUPERVISOR ->  {
                holder.itemView.SwitchRol.isChecked=true
                holder.itemView.lbl_supervisor.isChecked=true
                holder.itemView.lbl_supervisor.text= "Cuenta Activada"
            }
            Constants.ROLE_ADMINISTRADOR -> {
                holder.itemView.lbl_supervisor.isChecked=true
                holder.itemView.SwitchRol.isChecked=false
                holder.itemView.lbl_supervisor.text= "Cuenta Activada"
            }
            else->{
                holder.itemView.lbl_supervisor.text= "Cuenta Desactivada"
                holder.itemView.lbl_supervisor.isChecked=false
                holder.itemView.SwitchRol.isChecked=false
            }
        }
        Log.e("error","${holder.itemView.lbl_supervisor.isChecked}")

            holder.itemView.SwitchRol.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if(holder.itemView.lbl_supervisor.isChecked){
                    if (isChecked){
                        viewModel.updateUsuariosSuperAdmin(user._id, ROLE_SUPERVISOR)
                    }else{
                        viewModel.updateUsuariosSuperAdmin(user._id, ROLE_ADMINISTRADOR)
                    }
                }else{
                    holder.itemView.SwitchRol.isChecked=false
                    Log.e("false","no entro")
                    Snackbar.make(contentView,"Activa la cuenta", Snackbar.LENGTH_LONG).also { snackbar ->
                        snackbar.setAction("Ok"){
                            snackbar.dismiss()
                        }.show()
                    }
//                    Toast.makeText(holder.itemView.context,"Activa la cuenta",Toast.LENGTH_LONG).show()
                }
            })

        holder.itemView.lbl_supervisor.setOnCheckedChangeListener { buttonView, isChecked ->
           if (isChecked){
               if (holder.itemView.SwitchRol.isChecked){
                   viewModel.updateUsuariosSuperAdmin(user._id, ROLE_SUPERVISOR)
               }else{
                   viewModel.updateUsuariosSuperAdmin(user._id, ROLE_ADMINISTRADOR)
               }
           }else{
               viewModel.updateUsuariosSuperAdmin(user._id,"")
           }
        }




    }
    fun updateData(data: List<ValidateUsuarios>){
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