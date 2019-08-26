package com.android.githubproject.ui.main.stargazers

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.githubproject.BR

import com.android.githubproject.R
import com.android.githubproject.data.entity.Repository
import com.android.githubproject.data.entity.Stargazer
import com.android.githubproject.databinding.FragmentStargazersBinding
import com.android.githubproject.ui.base.BaseFragment
import com.android.githubproject.ui.main.MainViewModel

class StargazersFragment : BaseFragment<FragmentStargazersBinding, MainViewModel>(),
    StargazersAdapter.StargazersAdapterInteractionListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var fragmentStargazersBinding: FragmentStargazersBinding
    private var listener: OnFragmentInteractionListener? = null

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_stargazers
    }

    override fun getViewModel(): MainViewModel {
        activity?.let {
            mainViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
        }
        return mainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentStargazersBinding = getViewDataBinding()
        fragmentStargazersBinding.lifecycleOwner = activity

        val adapter = StargazersAdapter(this)
        val lm = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        fragmentStargazersBinding.rvFragmentStargazers.adapter = adapter
        fragmentStargazersBinding.rvFragmentStargazers.layoutManager = lm
        fragmentStargazersBinding.rvFragmentStargazers.setHasFixedSize(true)
        fragmentStargazersBinding.rvFragmentStargazers.isNestedScrollingEnabled = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onStargazersItemClicked(stargazers: Pair<Stargazer?, MutableList<Repository>?>) {
        if (stargazers.first != null)
            mainViewModel.setClickedItem(stargazers.first?.avatarUrl, stargazers.first?.name, null)
    }

    override fun onStargazersRowItemClicked(repo: Repository) {
        mainViewModel.setClickedItem(repo.owner.avatarUrl, repo.name, repo.owner.name)
    }

    interface OnFragmentInteractionListener

    companion object {
        @JvmStatic
        fun newInstance() = StargazersFragment()
    }
}
