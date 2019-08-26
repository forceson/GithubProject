package com.android.githubproject.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.android.AndroidInjection

abstract class BaseActivity<out T: ViewDataBinding, out V: BaseViewModel> : AppCompatActivity() {
    private lateinit var viewDataBinding : T
    private lateinit var viewModel : V
    
    abstract fun getBindingVariable() : Int
    abstract fun getLayoutId() : Int
    abstract fun getViewModel() : V

    fun getViewDataBinding(): T {
        return viewDataBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = getViewModel()
        viewDataBinding.setVariable(getBindingVariable(), viewModel)
        viewDataBinding.executePendingBindings()
    }
}