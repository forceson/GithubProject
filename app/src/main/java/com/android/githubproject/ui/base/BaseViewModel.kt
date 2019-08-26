package com.android.githubproject.ui.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.android.githubproject.ui.main.MainNavigator
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    protected var compositeDisposable : CompositeDisposable = CompositeDisposable()
    var isLoading: ObservableBoolean = ObservableBoolean(false)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}