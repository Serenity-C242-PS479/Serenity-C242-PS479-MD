package com.serenity.serenityapp.ui.login

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.serenity.serenityapp.data.model.api.request.LoginRequest
import com.serenity.serenityapp.databinding.ActivityLoginBinding
import com.serenity.serenityapp.helper.ViewModelFactory
import com.serenity.serenityapp.helper.checkAccessbility
import com.serenity.serenityapp.helper.checkUsageStatsPermission
import com.serenity.serenityapp.helper.moveToActivity
import com.serenity.serenityapp.service.ActivityMonitorService
import com.serenity.serenityapp.ui.main.MainActivity
import com.serenity.serenityapp.ui.register.RegisterActivity
import com.serenity.serenityapp.ui.setup.SetupActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var isAccessibilityEnabled: Boolean = false
    private var isDrawOverAppEnabled: Boolean = false
    private var isUsageAccessEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListener()
        setupOnClickListener()
        checkPermissions()
        setupViewModel()
        setupObserver()

        askNotificationPermission()
    }

    private fun setupViewModel() {
        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private fun setupObserver() {
        viewModel.token.observe(this) { token ->
            val isAllPermissionEnabled = isAccessibilityEnabled && isDrawOverAppEnabled && isUsageAccessEnabled

            if (token.isNotEmpty()) {
                if (isAllPermissionEnabled) moveToActivity(MainActivity::class.java)
                else moveToActivity(SetupActivity::class.java)
            }

        }

        viewModel.loading.observe(this) { loading ->
            binding.llLoader.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (error.isNotEmpty()) Toast.makeText(this@LoginActivity, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissions() {
        isAccessibilityEnabled = this.checkAccessbility(ActivityMonitorService::class.java)
        isDrawOverAppEnabled = Settings.canDrawOverlays(this)
        isUsageAccessEnabled = this.checkUsageStatsPermission()
    }

    private fun setupOnClickListener() {
        binding.tvRegister.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
    }

    private fun setupButtonListener() {
        binding.edtEmail.liveError.observe(this) {
            setEnableButton()
        }

        binding.edtPassword.liveError.observe(this) {
            setEnableButton()
        }
    }

    private fun setEnableButton() {
        val emailError = binding.edtEmail.liveError.value
        val passwordError = binding.edtPassword.liveError.value
        val isEnabled = (emailError == null && passwordError == null)

        binding.btnLogin.isEnabled = isEnabled
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }

    private fun login() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        val loginRequest = LoginRequest(email, password)

        viewModel.login(loginRequest)
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.tvRegister -> moveToActivity(RegisterActivity::class.java)

            binding.btnLogin -> {
                if (binding.btnLogin.isEnabled) login()
            }
        }
    }
}