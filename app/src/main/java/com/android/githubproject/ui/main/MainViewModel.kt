package com.android.githubproject.ui.main

import androidx.lifecycle.MutableLiveData
import com.android.githubproject.data.entity.Repository
import com.android.githubproject.data.entity.Stargazer
import com.android.githubproject.domain.GetReposUseCase
import com.android.githubproject.domain.GetStargazerUseCase
import com.android.githubproject.ui.base.BaseViewModel
import io.reactivex.Observable

class MainViewModel(
    private val getReposUseCase: GetReposUseCase,
    private val getStargazerUseCase: GetStargazerUseCase
) : BaseViewModel() {
    var navigator: MainNavigator? = null
    var username = "googlesamples"
    val clickedItemImageUrl = MutableLiveData("")
    val clickedItemName = MutableLiveData("")
    val clickedItemOwnerName = MutableLiveData("")
    val reposTabDataList = MutableLiveData<MutableList<Pair<Repository?, List<Stargazer>?>>>()
    val stargazersTabDataList = MutableLiveData<MutableList<Pair<Stargazer?, MutableList<Repository>?>>>()

    val repos = mutableListOf<Repository>()
    val stargazers = mutableListOf<Stargazer>()
    val stargazerRepos = mutableListOf<MutableList<Repository>>()
    val stargazerSet = hashSetOf<Stargazer>()

    var isLastPage = false
    var isLoadingFooterAdded = MutableLiveData(false)
    var currentPage = MutableLiveData(1)

    companion object {
        const val TOTAL_PAGES = 2
    }

    init {
        reposTabDataList.value = mutableListOf()
        stargazersTabDataList.value = mutableListOf()
    }

    fun onUserInfoFragmentClicked() {
        if (!clickedItemName.value.equals(""))
            navigator?.startNewActivity()
    }

    fun setClickedItem(url: String?, name: String?, ownerName: String?) {
        clickedItemImageUrl.value = url
        clickedItemName.value = name
        clickedItemOwnerName.value = ownerName
    }

    fun fetchData() {
        val reposSingle = getReposUseCase.execute(username, currentPage.value!!).doOnError {
            // TODO: Show SnackBar
            it.stackTrace
        }

        // TODO: 코드 정리 subscribe 하나로 교체. Side Effect 모두 제거.
        isLoading.set(true)
        compositeDisposable.addAll(
            reposSingle.flattenAsObservable { it }
                .doOnNext { repo ->
                    repos.add(repo)
                    getStargazerUseCase.execute(username, repo.name)
                        .doOnSuccess { list ->
                            Observable.just(list)
                                .flatMapIterable { it }
                                .map { it }
                                .subscribe({ updateStargazersTabDataList(it, repo) }, { it.stackTrace })
                        }
                        .map { Pair<Repository?, List<Stargazer>?>(null, it) }
                        .subscribe({ addStargazersToRepo(it, repo) }, { it.stackTrace })
                }
                .flatMap { repo ->
                    getStargazerUseCase.execute(username, repo.name)
                    return@flatMap Observable.just(
                        Pair<Repository?, List<Stargazer>?>(repo, null),
                        Pair<Repository?, List<Stargazer>?>(null, null)
                    )
                }
                .toList()
                .subscribe(
                    { updateReposTabDataList(it as List<Pair<Repository?, List<Stargazer>>>) },
                    { it.stackTrace })
        )
    }

    private fun updateReposTabDataList(it: List<Pair<Repository?, List<Stargazer>>>) {
        isLoadingFooterAdded.value = false
        val temp = reposTabDataList.value
        temp?.addAll(it)
        this.reposTabDataList.value = temp
        isLoading.set(false)
        if (currentPage.value!! >= TOTAL_PAGES) isLastPage = true
        else {
            isLoadingFooterAdded.value = true
        }
    }

    private fun addStargazersToRepo(
        it: Pair<Repository?, List<Stargazer>?>,
        repo: Repository
    ) {
        val temp = this.reposTabDataList.value
        temp!![(repos.indexOf(repo) * 2) + 1] = it
        this.reposTabDataList.value = temp
    }

    private fun updateStargazersTabDataList(
        stargazer: Stargazer,
        repo: Repository
    ) {
        // 보여줄 데이터 검색해서 stargazer 이름있으면 넣고 없으면 새로 생성 후 repo 넣고.
        if (stargazerSet.contains(stargazer)) {
            val temp = this.stargazersTabDataList.value
            temp?.get((getIndexOf(stargazers, stargazer) * 2) + 1)?.second?.add(repo)
            stargazersTabDataList.value = temp
        } else {
            stargazerSet.add(stargazer)
            stargazers.add(stargazer)
            stargazerRepos.add(mutableListOf(repo))
            val temp = this.stargazersTabDataList.value
            temp?.add(Pair(stargazer, null))
            temp?.add(Pair(null, mutableListOf(repo)))
            stargazersTabDataList.value = temp
        }
    }

    private fun getIndexOf(stargazers: MutableList<Stargazer>, stargazer: Stargazer?): Int {
        for (i in 0 until stargazers.size) {
            if (stargazer?.avatarUrl?.equals(stargazers[i].avatarUrl)!! &&
                stargazer.name.equals(stargazers[i].name)
            )
                return i
        }
        return -1
    }
}