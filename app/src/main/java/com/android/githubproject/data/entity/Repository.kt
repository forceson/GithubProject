package com.android.githubproject.data.entity

import com.google.gson.annotations.SerializedName

data class Repository(
    val name: String,
    val description: String,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    val owner: Owner
) {
    data class Owner(
        @SerializedName("avatar_url") val avatarUrl: String,
        @SerializedName("login") val name: String
    )
}
