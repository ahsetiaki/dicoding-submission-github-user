package com.setiaki.githubusersubmission2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.setiaki.githubusersubmission2.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed( {
            val toMainIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(toMainIntent)
            finish()
        }, 3000)
    }
}