package com.setiaki.githubusersubmission2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.AVATAR_URL
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.COMPANY
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.FOLLOWERS
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.FOLLOWING
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.LOCATION
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.LOGIN
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.NAME
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.PUBLIC_REPOS
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion._ID

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbgithubuserapp"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                "($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$LOGIN TEXT NOT NULL UNIQUE," +
                "$NAME TEXT," +
                "$AVATAR_URL TEXT NOT NULL," +
                "$FOLLOWERS INTEGER NOT NULL," +
                "$FOLLOWING INTEGER NOT NULL," +
                "$PUBLIC_REPOS INTEGER NOT NULL," +
                "$COMPANY TEXT," +
                "$LOCATION TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}