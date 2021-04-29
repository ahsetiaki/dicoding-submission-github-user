package com.setiaki.githubusersubmission2

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.recyclerview.widget.LinearLayoutManager
import com.setiaki.githubusersubmission2.databinding.FragmentUserListBinding
import com.setiaki.githubusersubmission2.db.DatabaseContract
import com.setiaki.githubusersubmission2.entity.UserDetail
import com.setiaki.githubusersubmission2.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class UserListFragment : Fragment() {
    companion object {
        private const val EXTRA_TYPE = "type"
        private const val EXTRA_STATE = "extra_state"
        const val TYPE_FOLLOWERS = "followers"
        const val TYPE_FOLLOWING = "following"
        const val TYPE_FAVORITE = "favorite"

        @JvmStatic
        fun newInstance(type: String) =
            UserListFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_TYPE, type)
                }
            }
    }

    private lateinit var userListAdapter: UserListAdapter
    private val userViewModel: UserViewModel by activityViewModels()

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private var type: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            type = this.getString(EXTRA_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvUserList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = setUserListAdapter()
        }
        updateUserList(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, userListAdapter.userListData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUserListAdapter(): UserListAdapter {
        userListAdapter = UserListAdapter().apply {
            notifyDataSetChanged()
            setOnItemClick(object : UserListAdapter.OnItemClick {
                override fun onItemClicked(username: String) {
                    val toDetailActivityIntent = Intent(activity, DetailActivity::class.java)
                    toDetailActivityIntent.putExtra(DetailActivity.EXTRA_USERNAME, username)
                    activity?.startActivity(toDetailActivityIntent)
                }
            })
        }
        return userListAdapter
    }

    private fun updateUserList(savedInstanceState: Bundle?) {
        with(userViewModel) {
            when (type) {
                TYPE_FOLLOWERS -> {
                    userDetail.observe(viewLifecycleOwner, { userDetail ->
                        if (userDetail != null) {
                            showLoading(true)
                            this.setUserList(userDetail.login, TYPE_FOLLOWERS)
                        }

                    })
                    userFollowersList.observe(viewLifecycleOwner, { userList ->
                        if (userList != null) {
                            userListAdapter.userListData = ArrayList(userList)
                            showLoading(false)
                            if (userList.isNotEmpty()) showList(true) else showList(false)
                        }
                    })
                }
                TYPE_FOLLOWING -> {
                    userDetail.observe(viewLifecycleOwner, { userDetail ->
                        if (userDetail != null) {
                            showLoading(true)
                            this.setUserList(userDetail.login, TYPE_FOLLOWING)
                        }

                    })
                    userFollowingList.observe(viewLifecycleOwner, { userList ->
                        if (userList != null) {
                            userListAdapter.userListData = ArrayList(userList)
                            showLoading(false)
                            if (userList.isNotEmpty()) showList(true) else showList(false)
                        }
                    })
                }
                TYPE_FAVORITE -> {
                    val handlerThread = HandlerThread("DataObserver")
                    handlerThread.start()
                    val handler = Handler(handlerThread.looper)

                    val contentObserver = object : ContentObserver(handler) {
                        override fun onChange(selfChange: Boolean) {
                            loadUserListAsync()
                        }
                    }

                    activity?.contentResolver?.registerContentObserver(
                        DatabaseContract.UserColumns.CONTENT_URI,
                        true,
                        contentObserver
                    )


                    if (savedInstanceState == null) {
                        loadUserListAsync()

                    } else {
                        savedInstanceState.getParcelableArrayList<UserDetail>(EXTRA_STATE)
                            ?.also { userListAdapter.userListData = it }
                    }


                }
                else -> {
                    searchQuery.observe(viewLifecycleOwner, { searchQuery ->
                        showLoading(true)
                        if (searchQuery != null) this.setUserList(searchQuery)
                    })
                    userSearchList.observe(viewLifecycleOwner, { userList ->
                        if (userList != null) {
                            userListAdapter.userListData = ArrayList(userList)
                            showLoading(false)
                            if (userList.isNotEmpty()) showList(true) else showList(false)
                        }
                    })
                }
            }
        }
    }

    private fun loadUserListAsync() {
        lifecycleScope.launch(Dispatchers.Main) {
            whenCreated {
                showLoading(true)
                val deferredUser = async(Dispatchers.IO) {
                    val cursor = activity?.contentResolver?.query(
                        DatabaseContract.UserColumns.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                    )
                    MappingHelper.mapCursorToArrayList(cursor)
                }

                val userList = deferredUser.await()
                userListAdapter.userListData = userList
                showLoading(false)
                if (userList.isNotEmpty()) showList(true) else showList(false)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.apply {
            if (state) {
                tvNotFound.visibility = View.GONE
                rvUserList.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showList(isListNotEmpty: Boolean) {
        binding.apply {
            if (isListNotEmpty) {
                rvUserList.visibility = View.VISIBLE
            } else {
                tvNotFound.visibility = View.VISIBLE
            }
        }
    }
}