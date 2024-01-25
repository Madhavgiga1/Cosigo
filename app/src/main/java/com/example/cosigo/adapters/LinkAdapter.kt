package com.example.cosigo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cosigo.databinding.LinkrowbindingBinding

class LinkAdapter: RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {
    //var links= emptyList<>()

    class LinkViewHolder(private val binding:LinkrowbindingBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(){


        }
        companion object{
            fun from(parent: ViewGroup):LinkViewHolder{
                val layoutInflater= LayoutInflater.from(parent.context)
                val binding=LinkrowbindingBinding.inflate(layoutInflater,parent,false)
                return LinkViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):LinkViewHolder {
        return LinkViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder:LinkViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}