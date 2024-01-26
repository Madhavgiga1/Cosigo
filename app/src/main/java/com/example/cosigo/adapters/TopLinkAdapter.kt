package com.example.cosigo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cosigo.databinding.TopLinkRowLayoutBinding
import com.example.cosigo.models.TopLink

class TopLinkAdapter: RecyclerView.Adapter<TopLinkAdapter.LinkViewHolder>() {
    var links= emptyList<TopLink>()

    class LinkViewHolder(private val binding: TopLinkRowLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(topLink: TopLink){
            binding.toplink=topLink
            binding.executePendingBindings()

        }
        companion object{
            fun from(parent: ViewGroup):LinkViewHolder{
                val layoutInflater= LayoutInflater.from(parent.context)
                val binding=TopLinkRowLayoutBinding.inflate(layoutInflater,parent,false)
                return LinkViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):LinkViewHolder {
        return LinkViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder:LinkViewHolder, position: Int) {
        val current_top_link=links[position]
        holder.bind(current_top_link)
    }

    override fun getItemCount(): Int {
        return links.size
    }
}