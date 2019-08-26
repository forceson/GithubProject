package com.android.githubproject.ui.main.stargazers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.githubproject.data.entity.Repository
import com.android.githubproject.databinding.ItemStargazerRepoViewBinding

class StargazerRepoAdapter(
    val listener: StargazerRepoAdapterInteractionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data = mutableListOf<Repository>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemStargazerRepoViewBinding =
            ItemStargazerRepoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StargazerRepoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder: StargazerRepoViewHolder = holder as StargazerRepoViewHolder
        holder.bind(data[position])
    }

    fun updateList(repos: List<Repository>) {
//        if (repos.size != data.size) {
        data.clear()
        data.addAll(repos)
        notifyItemRangeChanged(0, repos.size)
//        }
    }

    inner class StargazerRepoViewHolder(
        val binding: ItemStargazerRepoViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener { listener.onItemClicked(data[adapterPosition]) }
        }

        fun bind(item: Repository) {
            binding.data = item
        }
    }

    interface StargazerRepoAdapterInteractionListener {
        fun onItemClicked(repo: Repository)
    }
}