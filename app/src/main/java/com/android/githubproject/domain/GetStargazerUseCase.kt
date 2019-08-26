package com.android.githubproject.domain

import com.android.githubproject.data.entity.Stargazer
import com.android.githubproject.data.remote.ApiModule
import com.android.githubproject.util.rx.SchedulerProvider
import io.reactivex.Single

class GetStargazerUseCase(
    private val schedulerProvider: SchedulerProvider
) {
    fun execute(username: String, repoName: String): Single<List<Stargazer>> {
        return ApiModule.getClient().getStargazers(username, repoName)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}