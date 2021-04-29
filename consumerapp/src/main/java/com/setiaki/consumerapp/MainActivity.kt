package com.setiaki.consumerapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.setiaki.consumerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Consumer Users"

        if (savedInstanceState == null) {
            val searchListFragment = UserListFragment()
            supportFragmentManager.beginTransaction()
                .add(binding.root.id, searchListFragment)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menus, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = resources.getString(R.string.query_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    userViewModel.setSearchQuery(query!!)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val favoriteActivityIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(favoriteActivityIntent)
                true
            }
            R.id.settings -> {
                val settingsActivityIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(settingsActivityIntent)
                return true
            }
            else -> true
        }
    }

}