package com.android.githubproject.ui.main.stargazers

import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.githubproject.data.entity.Repository
import com.android.githubproject.data.entity.Stargazer
import com.android.githubproject.databinding.ItemStargazerReposViewBinding
import com.android.githubproject.databinding.ItemStargazersViewBinding

class StargazersAdapter(
    val listener: StargazersAdapterInteractionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data = mutableListOf<Pair<Stargazer?, MutableList<Repository>?>>()
    private val positionList = SparseIntArray()
    private val viewPool = RecyclerView.RecycledViewPool()

    companion object {
        const val STARGAZERS_TYPE = 0
        const val REPOSITORY_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            REPOSITORY_TYPE -> {
                val binding: ItemStargazerReposViewBinding =
                    ItemStargazerReposViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return StargazerReposViewHolder(binding)
            }
            else -> {
                val binding: ItemStargazersViewBinding =
                    ItemStargazersViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return StargazersViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (data[position].first != null) {
            val holder: StargazersViewHolder = holder as StargazersViewHolder
            holder.bind(data[position])
        } else if (data[position].second != null) {
            val holder: StargazerReposViewHolder = holder as StargazerReposViewHolder
            holder.bind(data[position])
            holder.adjustRecyclerViewItemPosition(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].first != null) {
            STARGAZERS_TYPE
        } else {
            REPOSITORY_TYPE
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is StargazerReposViewHolder) {
            val stargazerReposViewHolder: StargazerReposViewHolder = holder
            val position: Int = stargazerReposViewHolder.adapterPosition
            val firstVisiblePosition: Int = stargazerReposViewHolder.lm.findFirstVisibleItemPosition()
            positionList.put(position, firstVisiblePosition)
        }
        super.onViewRecycled(holder)
    }

    fun updateList(stargazers: List<Pair<Stargazer?, MutableList<Repository>?>>) {
        data.clear()
        data.addAll(stargazers)
        notifyDataSetChanged()
    }

    inner class StargazersViewHolder(
        val binding: ItemStargazersViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener { listener.onStargazersItemClicked(data[adapterPosition]) }
        }

        fun bind(item: Pair<Stargazer?, MutableList<Repository>?>) {
            binding.data = item.first
        }
    }

    inner class StargazerReposViewHolder(
        val binding: ItemStargazerReposViewBinding
    ) : RecyclerView.ViewHolder(binding.root), StargazerRepoAdapter.StargazerRepoAdapterInteractionListener {
        override fun onItemClicked(repo: Repository) {
            listener.onStargazersRowItemClicked(repo)
        }

        val adapter = StargazerRepoAdapter(this)
        val lm = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)

        init {
            binding.rvItemStargazerReposView.adapter = adapter
            binding.rvItemStargazerReposView.layoutManager = lm
            binding.rvItemStargazerReposView.setHasFixedSize(true)
            binding.rvItemStargazerReposView.isNestedScrollingEnabled = false
            binding.rvItemStargazerReposView.setRecycledViewPool(viewPool)
        }

        fun bind(item: Pair<Stargazer?, MutableList<Repository>?>) {
            if (item.second == null) {
                binding.pbItemStargazerReposView.visibility = View.VISIBLE
            } else {
                binding.pbItemStargazerReposView.visibility = View.GONE
            }
            item.second?.let { adapter.updateList(it) }
        }

        fun adjustRecyclerViewItemPosition(position: Int) {
            val lastSeenPosition = positionList.get(position)
            if (lastSeenPosition >= 0) {
                lm.scrollToPositionWithOffset(lastSeenPosition, 0)
            }
        }
    }

    interface StargazersAdapterInteractionListener {
        fun onStargazersItemClicked(stargazers: Pair<Stargazer?, MutableList<Repository>?>)
        fun onStargazersRowItemClicked(repo: Repository)
    }
}