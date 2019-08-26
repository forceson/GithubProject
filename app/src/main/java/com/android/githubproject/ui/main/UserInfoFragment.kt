package com.android.githubproject.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.android.githubproject.BR
import com.android.githubproject.R
import com.android.githubproject.ui.base.BaseFragment
import com.android.githubproject.databinding.FragmentUserInfoBinding

class UserInfoFragment : BaseFragment<FragmentUserInfoBinding, MainViewModel>() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var fragmentUserInfoBinding: FragmentUserInfoBinding

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_user_info
    }

    override fun getViewModel(): MainViewModel {
        activity?.let{
            mainViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
        }
        return mainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentUserInfoBinding = getViewDataBinding()
        fragmentUserInfoBinding.lifecycleOwner = activity
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

    interface OnFragmentInteractionListener

    companion object {
        @JvmStatic
        fun newInstance() = UserInfoFragment()
    }
}