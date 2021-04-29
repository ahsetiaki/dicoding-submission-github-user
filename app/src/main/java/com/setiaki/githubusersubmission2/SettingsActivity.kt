package com.setiaki.githubusersubmission2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.setiaki.githubusersubmission2.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.settings)

        if (savedInstanceState == null) {
            val settingsFragment = SettingsFragment()
            supportFragmentManager.beginTransaction()
                .add(binding.root.id, settingsFragment)
                .commit()
        }
    }
}