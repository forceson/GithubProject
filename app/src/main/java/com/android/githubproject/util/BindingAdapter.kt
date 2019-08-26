package com.android.githubproject.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.android.githubproject.data.entity.Repository
import com.android.githubproject.data.entity.Stargazer
import com.android.githubproject.ui.main.repos.ReposAdapter
import com.android.githubproject.ui.main.stargazers.StargazersAdapter
import com.bumptech.glide.Glide

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImageUrl(imageView: ImageView, url: String) {
        val context: Context = imageView.context
        Glide.with(context).load(url).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("page")
    fun updateCurrentPage(recyclerView: RecyclerView, page: Int) {
        val adapter: ReposAdapter? = recyclerView.adapter as? ReposAdapter
        adapter?.updateCurrentPage(page)
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun addRepos(recyclerView: RecyclerView, repos: MutableList<Pair<Repository?, List<Stargazer>?>>) {
        val adapter: ReposAdapter? = recyclerView.adapter as? ReposAdapter
        recyclerView.itemAnimator = null
        adapter?.updateList(repos)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun addStargazers(recyclerView: RecyclerView, stargazers: MutableList<Pair<Stargazer?, MutableList<Repository>?>>) {
        val adapter: StargazersAdapter? = recyclerView.adapter as? StargazersAdapter
        adapter?.updateList(stargazers)
    }
}