package com.android.githubproject.domain

import com.android.githubproject.data.entity.Repository
import com.android.githubproject.data.remote.ApiModule
import com.android.githubproject.util.rx.SchedulerProvider
import io.reactivex.Single

class GetReposUseCase(
    private val schedulerProvider: SchedulerProvider
) {
    fun execute(username: String, currentPage: Int): Single<List<Repository>> {
        return ApiModule.getClient().getRepos(username, currentPage)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}