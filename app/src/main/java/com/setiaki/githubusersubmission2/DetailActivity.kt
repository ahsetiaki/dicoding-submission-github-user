package com.setiaki.githubusersubmission2

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.setiaki.githubusersubmission2.databinding.ActivityDetailBinding
import com.setiaki.githubusersubmission2.db.DatabaseContract
import com.setiaki.githubusersubmission2.entity.UserDetail

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_STATUS_FAVORITE = "status_favorite"
        const val EXTRA_USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var sectionPagerAdapter: SectionPagerAdapter
    private lateinit var uriWithUsername: Uri
    private lateinit var userDetail: UserDetail
    private lateinit var username: String
    private val userViewModel: UserViewModel by viewModels()

    private var statusFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(EXTRA_USERNAME)!!
        uriWithUsername =
            Uri.parse(DatabaseContract.UserColumns.CONTENT_URI.toString() + "/" + username)
        supportActionBar?.title = username

        bindUserDetail()
        bindFollowersFollowingTabLayout()

        statusFavorite =
            savedInstanceState?.getBoolean(EXTRA_STATUS_FAVORITE) ?: getStatusFavorite()
        setFavoriteButtonState(statusFavorite)
        binding.fabFavorite.setOnClickListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_USERNAME, username)
        outState.putBoolean(EXTRA_STATUS_FAVORITE, statusFavorite)
    }

    private fun bindUserDetail() {
        userViewModel.setUserDetail(username)
        userViewModel.userDetail.observe(this, { userDetail ->
            if (userDetail != null) {
                with(binding) {
                    Glide.with(binding.root)
                        .load(userDetail.avatar_url)
                        .into(civAvatar)
                    tvUsername.text = userDetail.login
                    tvName.text = userDetail.name
                    tvFollowersCount.text =
                        resources.getString(R.string.followers_count, userDetail.followers)
                    tvFollowingCount.text =
                        resources.getString(R.string.following_count, userDetail.following)
                    tvRepositoryCount.text = userDetail.public_repos.let {
                        resources.getQuantityString(
                            R.plurals.repository_count,
                            it,
                            it,
                            it
                        )
                    }
                    tvCompany.text = userDetail.company
                    tvLocation.text = userDetail.location

                }
                this.userDetail = userDetail
            }
        })
    }

    private fun bindFollowersFollowingTabLayout() {
        sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager2 = binding.vp2FollowersFollowing
        viewPager2.adapter = sectionPagerAdapter
        val tabs = binding.tabsFollowersFollowing
        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fab_favorite) {
            toggleStatusFavorite(statusFavorite)
        }
    }

    private fun getStatusFavorite(): Boolean {
        val cursor = contentResolver.query(uriWithUsername, null, null, null, null)
        if (cursor != null && cursor.count > 0) {
            cursor.close()
            return true
        }
        return false
    }

    private fun toggleStatusFavorite(status: Boolean) {
        val toastMessage: String
        if (!status) {
            val contentValues = contentValuesOf(
                DatabaseContract.UserColumns.LOGIN to userDetail.login,
                DatabaseContract.UserColumns.NAME to userDetail.name,
                DatabaseContract.UserColumns.AVATAR_URL to userDetail.avatar_url,
                DatabaseContract.UserColumns.FOLLOWERS to userDetail.followers,
                DatabaseContract.UserColumns.FOLLOWING to userDetail.following,
                DatabaseContract.UserColumns.PUBLIC_REPOS to userDetail.public_repos,
                DatabaseContract.UserColumns.COMPANY to userDetail.company,
                DatabaseContract.UserColumns.LOCATION to userDetail.location
            )
            contentResolver.insert(DatabaseContract.UserColumns.CONTENT_URI, contentValues)
            toastMessage = resources.getString(R.string.toast_message_favorited, username)

        } else {
            contentResolver.delete(uriWithUsername, null, null)
            toastMessage = resources.getString(R.string.toast_message_unfavorited, username)
        }
        statusFavorite = !status
        setFavoriteButtonState(statusFavorite)
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()

    }

    private fun setFavoriteButtonState(status: Boolean) {
        if (status) {
            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_star_filled, null)
            binding.fabFavorite.setImageDrawable(drawable)
        } else {
            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_star_border, null)
            binding.fabFavorite.setImageDrawable(drawable)
        }
    }


}