package com.serenity.serenityapp.ui.setup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.serenity.serenityapp.databinding.ActivitySetupBinding
import com.serenity.serenityapp.helper.checkAccessbility
import com.serenity.serenityapp.helper.checkUsageStatsPermission
import com.serenity.serenityapp.service.ActivityMonitorService
import com.serenity.serenityapp.ui.main.MainActivity

class SetupActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySetupBinding
    private var isAccessibilityEnabled: Boolean = false
    private var isDrawOverAppEnabled: Boolean = false
    private var isUsageAccessEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()
        setupClickListener()
    }

    private fun slideInFromRight(view: View, duration: Long = 300L) {
        view.translationX = view.width.toFloat()
        view.isVisible = true
        view.animate()
            .translationX(0f)
            .setDuration(duration)
            .start()
    }

    private fun slideOutToLeft(view: View, duration: Long = 300L, onEnd: () -> Unit) {
        view.animate()
            .translationX(-view.width.toFloat())
            .setDuration(duration)
            .withEndAction {
                view.isVisible = false
                onEnd()
            }
            .start()
    }


    private fun checkPermissions() {
        isAccessibilityEnabled = this.checkAccessbility(ActivityMonitorService::class.java)
        isDrawOverAppEnabled = Settings.canDrawOverlays(this)
        isUsageAccessEnabled = this.checkUsageStatsPermission()

        showPermissionContainer()
    }

    private fun showPermissionContainer() {
        if (!isUsageAccessEnabled) {
            slideInFromRight(binding.containerUa)
            return
        }

        if (!isAccessibilityEnabled) {
            slideOutToLeft(binding.containerUa) {
                slideInFromRight(binding.containerAcb)
            }
            return
        }

        if (!isDrawOverAppEnabled) {
            slideOutToLeft(binding.containerAcb) {
                slideInFromRight(binding.containerSp)
            }
            return
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupClickListener() {
        binding.btnAcbEnable.setOnClickListener(this)
        binding.btnUaEnable.setOnClickListener(this)
        binding.btnSpEnable.setOnClickListener(this)
    }

    private fun requestAccessibility() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    private fun requestDrawOverApp() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, 100)
    }

    private fun requestUsageStatsPermission() {
        if (!checkUsageStatsPermission()) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnUaEnable -> {
                requestUsageStatsPermission()
            }

            binding.btnAcbEnable -> {
                requestAccessibility()
            }

            binding.btnSpEnable -> {
                requestDrawOverApp()
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        checkPermissions()
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) checkPermissions()
    }

}