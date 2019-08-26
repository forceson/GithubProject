package com.android.githubproject.ui.main

import androidx.lifecycle.ViewModelProvider
import com.android.githubproject.ViewModelProviderFactory
import com.android.githubproject.util.rx.AppSchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
    @Provides
    fun provideViewModelProviderFactory(appSchedulerProvider: AppSchedulerProvider) : ViewModelProvider.Factory{
        return ViewModelProviderFactory(appSchedulerProvider)
    }
}