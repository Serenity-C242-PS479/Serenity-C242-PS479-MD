package com.serenity.serenityapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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