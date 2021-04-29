package com.setiaki.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.setiaki.githubsubmission2"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val LOGIN = "username"
            const val NAME = "name"
            const val AVATAR_URL = "avatar_url"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val PUBLIC_REPOS = "public_repos"
            const val COMPANY = "company"
            const val LOCATION = "location"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}