package com.android.githubproject.di.builder

import com.android.githubproject.ui.github.GithubActivity
import com.android.githubproject.ui.github.GithubActivityModule
import com.android.githubproject.ui.main.MainActivity
import com.android.githubproject.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [GithubActivityModule::class])
    abstract fun bindGithubActivity(): GithubActivity
}