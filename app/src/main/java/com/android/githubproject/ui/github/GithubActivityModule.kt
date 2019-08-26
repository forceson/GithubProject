package com.android.githubproject.ui.github

import androidx.lifecycle.ViewModelProvider
import com.android.githubproject.ViewModelProviderFactory
import com.android.githubproject.util.rx.AppSchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class GithubActivityModule {
    @Provides
    fun provideViewModelProviderFactory(appSchedulerProvider: AppSchedulerProvider): ViewModelProvider.Factory {
        return ViewModelProviderFactory(appSchedulerProvider)
    }
}