package com.example.cosigo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cosigo.databinding.RecentLinkRowLayoutBinding
import com.example.cosigo.models.RecentLink

class RecentLinksAdapter: RecyclerView.Adapter<RecentLinksAdapter.MyViewHolder>() {
    var recentlinks= emptyList<RecentLink>()
    class MyViewHolder(private val binding:RecentLinkRowLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(recentLink: RecentLink){
            binding.recentlink=recentLink
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup):MyViewHolder{
                val layoutInflater= LayoutInflater.from(parent.context)
                val binding= RecentLinkRowLayoutBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current_recentlink=recentlinks[position]
        holder.bind(current_recentlink)
    }

    override fun getItemCount(): Int {
        return recentlinks.size
    }
}