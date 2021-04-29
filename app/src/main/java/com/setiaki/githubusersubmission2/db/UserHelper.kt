package com.setiaki.githubusersubmission2.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.LOGIN
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.setiaki.githubusersubmission2.db.DatabaseContract.UserColumns.Companion._ID
import java.sql.SQLException

class UserHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: UserHelper? = null

        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null
        )
    }

    fun queryByUsername(username: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$LOGIN = ?",
            arrayOf(username),
            null,
            null,
            null,
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByUsername(username: String): Int {
        return database.delete(DATABASE_TABLE, "$LOGIN = '$username'", null)
    }
}