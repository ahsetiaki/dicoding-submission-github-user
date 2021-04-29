package com.setiaki.githubusersubmission2


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.setiaki.githubusersubmission2.databinding.UserItemsBinding
import com.setiaki.githubusersubmission2.entity.UserDetail


class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {
    var userListData = ArrayList<UserDetail>()
        set(data) {
            if (field.size > 0) {
                field.clear()
            }
            field.addAll(data)
            notifyDataSetChanged()
        }
    private lateinit var onItemClick: OnItemClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val binding = UserItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val userItem = userListData[position]
        holder.bind(userItem)
    }

    override fun getItemCount(): Int = userListData.size

    inner class UserListViewHolder(private val binding: UserItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userDetail: UserDetail) = with(binding) {
            tvUsername.text = userDetail.login
            Glide.with(root.context)
                .load(userDetail.avatar_url)
                .into(civAvatar)
            root.setOnClickListener { onItemClick.onItemClicked(userDetail.login) }
        }
    }

    fun setOnItemClick(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    interface OnItemClick {
        fun onItemClicked(username: String)
    }
}