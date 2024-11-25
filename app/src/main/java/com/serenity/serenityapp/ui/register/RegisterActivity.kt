package com.serenity.serenityapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.serenity.serenityapp.R
import com.serenity.serenityapp.databinding.ActivityRegisterBinding
import com.serenity.serenityapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListener()
        setupOnClickListener()
    }

    private fun setupOnClickListener() {
        binding.tvLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
    }

    private fun setupButtonListener() {
        binding.edtName.liveText.observe(this) {
            setEnableButton()
        }

        binding.edtAge.liveText.observe(this) {
            setEnableButton()
        }

        binding.edtEmail.liveError.observe(this) {
            setEnableButton()
        }

        binding.edtPassword.liveError.observe(this) {
            setEnableButton()
        }
    }

    private fun setEnableButton() {
        val nameText = binding.edtName.liveText.value
        val ageText = binding.edtAge.liveText.value
        val emailError = binding.edtEmail.liveError.value
        val passwordError = binding.edtPassword.liveError.value
        val isEnabled = (!nameText.isNullOrEmpty() &&
                !ageText.isNullOrEmpty() &&
                emailError == null &&
                passwordError == null)

        binding.btnRegister.isEnabled = isEnabled
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.tvLogin -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            binding.btnRegister -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}