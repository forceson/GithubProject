package com.android.githubproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.githubproject.domain.GetReposUseCase
import com.android.githubproject.domain.GetStargazerUseCase
import com.android.githubproject.ui.main.MainViewModel
import com.android.githubproject.util.rx.SchedulerProvider
import java.lang.IllegalArgumentException
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ViewModelProviderFactory @Inject constructor(private val schedulerProvider: SchedulerProvider) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(
                        GetReposUseCase(schedulerProvider),
                        GetStargazerUseCase(schedulerProvider)
                    )
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}