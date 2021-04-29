package com.setiaki.githubusersubmission2.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.setiaki.githubusersubmission2.db.DatabaseContract.AUTHORITY
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.setiaki.githubusersubmission2.db.UserHelper

class UserProvider : ContentProvider() {
    companion object {
        private const val USER = 1
        private const val USER_USERNAME = 2
        private lateinit var userHelper: UserHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", USER_USERNAME)
        }
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USER -> userHelper.queryAll()
            USER_USERNAME -> userHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> userHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER_USERNAME) {
            sUriMatcher.match(uri) -> userHelper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }


}