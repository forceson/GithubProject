package com.android.githubproject.ui.main.repos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.githubproject.data.entity.Stargazer
import com.android.githubproject.databinding.ItemRepoStargazerViewBinding

class RepoStargazerAdapter(
    val listener: RepoStargazerAdapterInteractionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data = mutableListOf<Stargazer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemRepoStargazerViewBinding =
            ItemRepoStargazerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoStargazerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder: RepoStargazerViewHolder = holder as RepoStargazerViewHolder
        holder.bind(data[position])
    }

    fun updateList(repos: List<Stargazer>) {
//        if (repos.size != data.size) {
        data.clear()
        data.addAll(repos)
        notifyItemRangeChanged(0, repos.size)
//        }
    }

    inner class RepoStargazerViewHolder(
        val binding: ItemRepoStargazerViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener { listener.onItemClicked(data[adapterPosition]) }
        }

        fun bind(item: Stargazer) {
            binding.data = item
        }
    }

    interface RepoStargazerAdapterInteractionListener {
        fun onItemClicked(stargazer: Stargazer)
    }
}