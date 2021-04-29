package com.setiaki.consumerapp.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetail(
    val id: Int = 0,
    val login: String = "",
    val name: String? = null,
    val avatar_url: String = "",
    val followers: Int = 0,
    val following: Int = 0,
    val public_repos: Int = 0,
    val company: String? = null,
    val location: String? = null
) : Parcelable
