package com.consorciosm.sanmiguel.ui.main.admin.ui.monitoreo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.consorciosm.sanmiguel.R
import kotlinx.android.synthetic.main.layout_type_map.view.*

class TypeMaps(private val listener: (position:Int) -> Unit): RecyclerView.Adapter<TypeMaps.ViewHolder>() {
    private val listResource: List< Int> = listOf(R.drawable.type_default,R.drawable.type_satellite,R.drawable.type_terrain,R.drawable.type_hibrid)
    private val listTitle: List< String> = listOf("Defecto","Satelite","Terreno","Hibrido")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.layout_type_map,parent,false))

    private var optionSelected = 0

    override fun getItemCount() = listResource.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        holder.itemView.selected_item.visibility = if(position ==optionSelected ) View.VISIBLE else View.GONE
        holder.itemView.text_view_title.text=listTitle[position]

        Glide.with(holder.itemView.context).load(listResource[position]).into(  holder.itemView.image_type)
        listTitle
        holder.itemView.setOnClickListener {
            optionSelected=position
            listener(position)
            notifyDataSetChanged()
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){




    }

}