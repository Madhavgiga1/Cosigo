package com.example.cosigo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cosigo.databinding.TopLinkRowLayoutBinding
import com.example.cosigo.models.Link
import com.example.cosigo.util.LinkDiffUtil



class LinkAdapter: RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {
    var links= emptyList<Link>()

    class LinkViewHolder(private val binding: TopLinkRowLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(Link:Link){
            binding.link=Link
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
        val current_link=links[position]
        holder.bind(current_link)
    }

    override fun getItemCount(): Int {
        return links.size
    }

    //for diff util
    fun setData(newData: List<Link>){
        val linksDiffUtil =
            LinkDiffUtil(links, newData)
        val diffUtilResult = DiffUtil.calculateDiff(linksDiffUtil)
        links = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}