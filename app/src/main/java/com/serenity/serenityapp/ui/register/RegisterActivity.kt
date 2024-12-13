package com.serenity.serenityapp.ui.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.model.api.request.RegisterRequest
import com.serenity.serenityapp.databinding.ActivityRegisterBinding
import com.serenity.serenityapp.helper.ViewModelFactory
import com.serenity.serenityapp.helper.moveToActivity
import com.serenity.serenityapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupObserver()
        setupButtonListener()
        setupOnClickListener()
    }

    private fun setupViewModel() {
        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
    }

    private fun setupObserver() {
        viewModel.registerResult.observe(this) { registerResult ->
            if (registerResult.isSuccess()) {
                moveToActivity(LoginActivity::class.java)
                Toast.makeText(this@RegisterActivity, "Successfully registered", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loading.observe(this) { loading ->
            binding.llLoader.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (error.isNotEmpty()) Toast.makeText(this@RegisterActivity, error, Toast.LENGTH_SHORT).show()
        }
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

    private fun register() {
        val name = binding.edtName.text.toString()
        val age = binding.edtAge.text.toString().toInt()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        val genderPosition = binding.edtGender.selectedItemPosition
        val genders = resources.getStringArray(R.array.gender_values)
        val gender = genders[genderPosition]

        val registerRequest = RegisterRequest(name, email, password, age, gender)

        viewModel.register(registerRequest)
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.tvLogin -> moveToActivity(LoginActivity::class.java)

            binding.btnRegister -> register()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveToActivity(LoginActivity::class.java)
    }
}