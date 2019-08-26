package com.android.githubproject.di.component

import android.app.Application
import com.android.githubproject.GithubApp
import com.android.githubproject.di.builder.ActivityBuilder
import com.android.githubproject.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBuilder::class])
interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(application: GithubApp)
}