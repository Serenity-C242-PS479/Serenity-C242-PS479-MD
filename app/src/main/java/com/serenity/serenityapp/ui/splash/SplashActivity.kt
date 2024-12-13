package com.serenity.serenityapp.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.serenity.serenityapp.databinding.ActivitySplashBinding
import com.serenity.serenityapp.helper.ViewModelFactory
import com.serenity.serenityapp.helper.checkAccessbility
import com.serenity.serenityapp.helper.checkUsageStatsPermission
import com.serenity.serenityapp.helper.moveToActivity
import com.serenity.serenityapp.service.ActivityMonitorService
import com.serenity.serenityapp.ui.login.LoginActivity
import com.serenity.serenityapp.ui.main.MainActivity
import com.serenity.serenityapp.ui.setup.SetupActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel
    private var isAccessibilityEnabled: Boolean = false
    private var isDrawOverAppEnabled: Boolean = false
    private var isUsageAccessEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupObserver()
        checkPermissions()

    }

    private fun setupViewModel() {
        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    private fun setupObserver() {
        viewModel.token.observe(this) { token ->


            if (token.isNotEmpty()) moveToSetupOrMain()
            else moveToLogin()

        }
    }

    private fun checkPermissions() {
        isAccessibilityEnabled = this.checkAccessbility(ActivityMonitorService::class.java)
        isDrawOverAppEnabled = Settings.canDrawOverlays(this)
        isUsageAccessEnabled = this.checkUsageStatsPermission()
    }

    private fun moveToLogin() {
        Handler().postDelayed({
            moveToActivity(LoginActivity::class.java)
        }, 1000)
    }

    private fun moveToSetupOrMain() {
        val isAllPermissionEnabled = isAccessibilityEnabled && isDrawOverAppEnabled && isUsageAccessEnabled

        Handler().postDelayed({
            if (isAllPermissionEnabled) moveToActivity(MainActivity::class.java)
            else moveToActivity(SetupActivity::class.java)
        }, 1000)
    }
}