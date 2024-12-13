package com.serenity.serenityapp.ui.challenge

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.model.api.request.ChallengeRequest
import com.serenity.serenityapp.databinding.ActivityCreateChallengeBinding
import com.serenity.serenityapp.helper.ViewModelFactory
import com.serenity.serenityapp.helper.moveToActivity
import com.serenity.serenityapp.ui.main.MainActivity

class CreateChallengeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCreateChallengeBinding
    private lateinit var viewModel: CreateChallengeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewListener()
        setupViewModel()
        setupObserver()
    }

    private fun setupViewModel() {
        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[CreateChallengeViewModel::class.java]
    }

    private fun setupObserver() {
        viewModel.isCreateSuccess.observe(this) { isCreateSuccess ->
            if (isCreateSuccess) {
                Toast.makeText(this, "Challenge successfully created", Toast.LENGTH_SHORT).show()
                moveToActivity(MainActivity::class.java)
            }
        }

        viewModel.loading.observe(this) { loading ->
            binding.llLoader.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (error.isNotEmpty()) Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewListener() {
        binding.edtStartHour.setOnFocusChangeListener {_, focused ->
            if (focused) showTimePicker { time ->
                binding.edtStartHour.setText(time)
                binding.edtStartHour.clearFocus()
            }
        }

        binding.edtEndHour.setOnFocusChangeListener {_, focused ->
            if (focused) showTimePicker { time ->
                binding.edtEndHour.setText(time)
                binding.edtEndHour.clearFocus()
            }
        }

        binding.btnSubmit.setOnClickListener(this)
    }

    private fun createChallenge() {
        val title = binding.edtTitle.text.toString()
        val startHour = binding.edtStartHour.text.toString()
        val endHour = binding.edtEndHour.text.toString()
        val status = "On Progress"

        val challengeRequest = ChallengeRequest(null, title, startHour, endHour, status)
        viewModel.createChallenge(challengeRequest)
    }

    private fun showTimePicker(callback: (time: String) -> Unit) {
        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            callback.invoke(selectedTime)
        },
            7, 0,
            true
        )
        timePickerDialog.show()
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnSubmit -> createChallenge()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveToActivity(MainActivity::class.java) {
            putExtra("menu", R.id.navigation_challenge)
        }
    }
}