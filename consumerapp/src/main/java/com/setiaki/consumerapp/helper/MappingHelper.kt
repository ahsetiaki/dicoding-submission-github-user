package com.setiaki.consumerapp.helper

import android.database.Cursor
import com.setiaki.consumerapp.db.DatabaseContract
import com.setiaki.consumerapp.entity.UserDetail

object MappingHelper {
    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<UserDetail> {
        val userList = ArrayList<UserDetail>()
        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOGIN))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val avatar_url =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
                val followers =
                    getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS))
                val following =
                    getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING))
                val public_repos =
                    getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.PUBLIC_REPOS))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY))
                val location =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
                val userDetail = UserDetail(
                    id,
                    login,
                    name,
                    avatar_url,
                    followers,
                    following,
                    public_repos,
                    company,
                    location
                )
                userList.add(userDetail)
            }
        }
        return userList
    }
}