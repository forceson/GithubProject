package com.android.githubproject.data.remote

class ApiEndPoint {
    companion object {
        const val URL_REPOS = "/users/{username}/repos"

        const val URL_STARGAZERS = "repos/{username}/{repoName}/stargazers"
    }
}