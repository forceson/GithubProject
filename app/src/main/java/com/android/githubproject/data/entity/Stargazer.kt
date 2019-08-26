package com.android.githubproject.data.entity

import com.google.gson.annotations.SerializedName

data class Stargazer(
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("login") val name: String
)