package com.android.githubproject.ui.main.repos

import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.githubproject.data.entity.Repository
import com.android.githubproject.data.entity.Stargazer
import com.android.githubproject.databinding.ItemLoadingViewBinding
import com.android.githubproject.databinding.ItemRepoStargazersViewBinding
import com.android.githubproject.databinding.ItemReposViewBinding
import com.android.githubproject.ui.main.MainViewModel.Companion.TOTAL_PAGES


class ReposAdapter(
    val listener: ReposAdapterInteractionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data = mutableListOf<Pair<Repository?, List<Stargazer>?>>()
    private val positionList = SparseIntArray()
    private val viewPool = RecyclerView.RecycledViewPool()
    private var isLoadingAdded: Boolean = false
    private var currentPage: Int = 1
    private var isFirstLoaded: MutableList<Boolean> = mutableListOf()

    companion object {
        const val REPOSITORY_TYPE = 0
        const val STARGAZERS_TYPE = 1
        const val LOADING_TYPE = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            REPOSITORY_TYPE -> {
                val binding: ItemReposViewBinding =
                    ItemReposViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ReposViewHolder(binding)
            }
            STARGAZERS_TYPE -> {
                val binding: ItemRepoStargazersViewBinding =
                    ItemRepoStargazersViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return RepoStargazersViewHolder(binding)
            }
            else -> {
                val binding: ItemLoadingViewBinding =
                    ItemLoadingViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return LoadingViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            REPOSITORY_TYPE -> {
                val reposViewHolder: ReposViewHolder = holder as ReposViewHolder
                reposViewHolder.bind(data[position])
            }
            STARGAZERS_TYPE -> {
                val repoStargazersViewHolder: RepoStargazersViewHolder = holder as RepoStargazersViewHolder
                repoStargazersViewHolder.bind(data[position])
                repoStargazersViewHolder.adjustRecyclerViewItemPosition(position)
            }
            else -> {
                holder as LoadingViewHolder
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            (position == data.size - 1 && isLoadingAdded) -> LOADING_TYPE
            data[position].first != null -> REPOSITORY_TYPE
            else -> STARGAZERS_TYPE
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is RepoStargazersViewHolder) {
            val repoStargazersViewHolder: RepoStargazersViewHolder = holder
            val position: Int = repoStargazersViewHolder.adapterPosition
            val firstVisiblePosition: Int = repoStargazersViewHolder.lm.findFirstVisibleItemPosition()
            positionList.put(position, firstVisiblePosition)
        }
        super.onViewRecycled(holder)
    }

    fun updateCurrentPage(page: Int) {
        this.currentPage = page
    }

    fun updateList(
        repos: List<Pair<Repository?, List<Stargazer>?>>
    ) {
        if (repos.isEmpty()) return
        // 0 - 59, 60 - 119
//        data.clear()
//        data.addAll(repos)
        if (currentPage > isFirstLoaded.size) {
            removeLoadingFooter()
            for (i in (currentPage - 1) * 60 until repos.size) {
                data.add(repos[i])
                notifyItemInserted(i)
            }
            if (currentPage != TOTAL_PAGES) addLoadingFooter()
//            notifyItemRangeInserted((currentPage - 1) * 60, repos.size)

            /*for (i in (currentPage - 1) * 60 until repos.size) {
                          data.add(repos[i])
                          notifyItemInserted(i)
                      }*/
            isFirstLoaded.add(true)
        } else {
            for (i in (currentPage - 1) * 60 until repos.size) {
                if (data[i].first == null && data[i].second == null) {
                    data[i] = repos[i]
                    notifyItemChanged(i)
                }
            }
        }
    }

    fun addLoadingFooter() {
        if (data.size > 0) {
            isLoadingAdded = true
            data.add(Pair(null, null))
            notifyItemInserted(data.size - 1)
        }
    }

    fun removeLoadingFooter() {
        if (data.size > 0) {
            isLoadingAdded = false
            data.removeAt(data.size - 1)
            notifyItemRemoved(data.size - 1)
        }
    }

    inner class ReposViewHolder(
        val binding: ItemReposViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener { listener.onReposItemClicked(data[adapterPosition]) }
        }

        fun bind(item: Pair<Repository?, List<Stargazer>?>) {
            binding.data = item.first
        }
    }

    inner class RepoStargazersViewHolder(
        val binding: ItemRepoStargazersViewBinding
    ) : RecyclerView.ViewHolder(binding.root), RepoStargazerAdapter.RepoStargazerAdapterInteractionListener {
        override fun onItemClicked(stargazer: Stargazer) {
            listener.onReposRowItemClicked(stargazer)
        }

        val adapter = RepoStargazerAdapter(this)
        val lm = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)

        init {
            binding.rvItemRepoStargazersView.adapter = adapter
            binding.rvItemRepoStargazersView.layoutManager = lm
            binding.rvItemRepoStargazersView.setHasFixedSize(true)
            binding.rvItemRepoStargazersView.isNestedScrollingEnabled = false
            binding.rvItemRepoStargazersView.setRecycledViewPool(viewPool)
        }

        fun bind(item: Pair<Repository?, List<Stargazer>?>) {
            if (item.second != null) {
                binding.pbItemRepoStargazersView.visibility = View.GONE
                if (item.second!!.isEmpty()) binding.rvItemRepoStargazersView.visibility = View.GONE
                else binding.rvItemRepoStargazersView.visibility = View.VISIBLE
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

    inner class LoadingViewHolder(
        val binding: ItemLoadingViewBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface ReposAdapterInteractionListener {
        fun onReposItemClicked(repos: Pair<Repository?, List<Stargazer>?>)
        fun onReposRowItemClicked(stargazer: Stargazer)
    }
}