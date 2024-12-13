package com.serenity.serenityapp.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.serenity.serenityapp.R
import com.serenity.serenityapp.databinding.ActivityMainBinding
import com.serenity.serenityapp.ui.challenge.ChallengeFragment
import com.serenity.serenityapp.ui.home.HomeFragment
import com.serenity.serenityapp.ui.setting.SettingFragment
import com.serenity.serenityapp.ui.usage.UsageFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFragment()
        setupNavigation()

        val defaultItemId = intent.getIntExtra("menu", R.id.navigation_home)
        binding.navigation.selectedItemId = defaultItemId

        askNotificationPermission()
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment())
            .commit()
    }

    private fun setupNavigation() {
        binding.navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment())
                        .commit()
                    true
                }

                R.id.navigation_challenge -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ChallengeFragment())
                        .commit()
                    true
                }

                R.id.navigation_activity -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, UsageFragment())
                        .commit()
                    true
                }

                R.id.navigation_setting -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SettingFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }

    fun changeSelectedNavigation(menuId: Int) {
        binding.navigation.selectedItemId = menuId
    }
}