package com.android.githubproject.di.module

import com.android.githubproject.util.rx.AppSchedulerProvider
import com.android.githubproject.util.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideSchedulersProvider(appSchedulerProvider: AppSchedulerProvider): SchedulerProvider {
        return appSchedulerProvider
    }
}