package com.serenity.serenityapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.serenity.serenityapp.R
import com.serenity.serenityapp.databinding.ActivityLoginBinding
import com.serenity.serenityapp.ui.main.MainActivity
import com.serenity.serenityapp.ui.personalization.PersonalizationActivity
import com.serenity.serenityapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListener()
        setupOnClickListener()
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

    override fun onClick(view: View?) {
        when(view) {
            binding.tvRegister -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }

            binding.btnLogin -> {
                if (binding.btnLogin.isEnabled) {
                    val intent = Intent(this, PersonalizationActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}