package com.setiaki.consumerapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.setiaki.consumerapp.entity.UserDetail
import com.setiaki.consumerapp.entity.UserSearchList
import cz.msebera.android.httpclient.Header

class UserViewModel : ViewModel() {
    private val mutableSearchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = mutableSearchQuery

    private val mutableUserSearchList = MutableLiveData<List<UserDetail>>()
    val userSearchList: LiveData<List<UserDetail>> get() = mutableUserSearchList

    private val mutableUserDetail = MutableLiveData<UserDetail>()
    val userDetail: LiveData<UserDetail> get() = mutableUserDetail

    private val mutableUserFollowersList = MutableLiveData<List<UserDetail>>()
    val userFollowersList: LiveData<List<UserDetail>> get() = mutableUserFollowersList

    private val mutableUserFollowingList = MutableLiveData<List<UserDetail>>()
    val userFollowingList: LiveData<List<UserDetail>> get() = mutableUserFollowingList


    fun setSearchQuery(query: String) {
        mutableSearchQuery.value = query
    }

    fun setUserList(query: String, type: String = "") {
        val url = when (type) {
            UserListFragment.TYPE_FOLLOWERS -> "https://api.github.com/users/$query/followers"
            UserListFragment.TYPE_FOLLOWING -> "https://api.github.com/users/$query/following"
            else -> "https://api.github.com/search/users?q=$query"
        }
        val token = "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"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val gson = Gson()
                    if (type == UserListFragment.TYPE_FOLLOWERS || type == UserListFragment.TYPE_FOLLOWING) {
                        val jsonAdapter = object : TypeToken<List<UserDetail>>() {}.type
                        val response = gson.fromJson<List<UserDetail>>(result, jsonAdapter)
                        when (type) {
                            UserListFragment.TYPE_FOLLOWERS -> {
                                mutableUserFollowersList.postValue(response)
                            }
                            UserListFragment.TYPE_FOLLOWING -> {
                                mutableUserFollowingList.postValue(response)
                            }
                        }
                    } else {
                        val response = gson.fromJson(result, UserSearchList::class.java)
                        mutableUserSearchList.postValue(response.items)
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int, headers:
                Array<out Header>?, responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("Exception", error?.message.toString())
            }
        })
    }

    fun setUserDetail(query: String) {
        val url = "https://api.github.com/users/$query"
        val token = "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"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val gson = Gson()
                    val response = gson.fromJson(result, UserDetail::class.java)
                    mutableUserDetail.postValue(response)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("Exception", error?.message.toString())
            }
        })
    }
}