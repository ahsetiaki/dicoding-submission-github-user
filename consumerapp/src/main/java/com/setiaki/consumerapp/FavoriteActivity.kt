package com.setiaki.consumerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.setiaki.consumerapp.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.favorite_title)

        if (savedInstanceState == null) {
            val favoriteUserListFragment =
                UserListFragment.newInstance(UserListFragment.TYPE_FAVORITE)
            supportFragmentManager.beginTransaction()
                .add(binding.root.id, favoriteUserListFragment)
                .commit()
        }
    }
}