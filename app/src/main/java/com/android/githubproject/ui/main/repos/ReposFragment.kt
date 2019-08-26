package com.android.githubproject.ui.main.repos

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.githubproject.BR

import com.android.githubproject.R
import com.android.githubproject.data.entity.Repository
import com.android.githubproject.data.entity.Stargazer
import com.android.githubproject.databinding.FragmentReposBinding
import com.android.githubproject.ui.base.BaseFragment
import com.android.githubproject.ui.main.MainViewModel
import com.android.githubproject.ui.main.MainViewModel.Companion.TOTAL_PAGES
import com.android.githubproject.util.PaginationScrollListener

class ReposFragment : BaseFragment<FragmentReposBinding, MainViewModel>(),
    ReposAdapter.ReposAdapterInteractionListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var fragmentReposBinding: FragmentReposBinding
    private var listener: OnFragmentInteractionListener? = null

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_repos
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
        fragmentReposBinding = getViewDataBinding()
        fragmentReposBinding.lifecycleOwner = activity

        val adapter = ReposAdapter(this)
        val lm = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        fragmentReposBinding.rvFragmentRepos.adapter = adapter
        fragmentReposBinding.rvFragmentRepos.layoutManager = lm
//        fragmentReposBinding.rvFragmentRepos.setHasFixedSize(true)
        fragmentReposBinding.rvFragmentRepos.isNestedScrollingEnabled = false
        fragmentReposBinding.rvFragmentRepos.addOnScrollListener(object : PaginationScrollListener(lm) {
            override fun loadMoreItems() {
                var currentPage: Int = mainViewModel.currentPage.value!!
                mainViewModel.currentPage.value = ++currentPage
                mainViewModel.fetchData()
            }

            override fun isLoading(): Boolean {
                return mainViewModel.isLoading.get()
            }

            override fun isLastPage(): Boolean {
                return mainViewModel.isLastPage
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }
        })
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

    override fun onReposItemClicked(repos: Pair<Repository?, List<Stargazer>?>) {
        if (repos.first != null)
            mainViewModel.setClickedItem(repos.first?.owner?.avatarUrl, repos.first?.name, repos.first?.owner?.name)
    }

    override fun onReposRowItemClicked(stargazer: Stargazer) {
        mainViewModel.setClickedItem(stargazer.avatarUrl, stargazer.name, null)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ReposFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = ReposFragment()
    }
}
