package com.serenity.serenityapp.ui.setting

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.serenity.serenityapp.R
import com.serenity.serenityapp.databinding.FragmentSettingBinding
import com.serenity.serenityapp.helper.ViewModelFactory
import com.serenity.serenityapp.helper.isNotificationListenerServiceEnabled
import com.serenity.serenityapp.helper.moveToActivity
import com.serenity.serenityapp.helper.openNotificationAccessSettings
import com.serenity.serenityapp.ui.login.LoginActivity
import com.serenity.serenityapp.ui.profile.ProfileActivity
import com.serenity.serenityapp.ui.sentiment.SentimentActivity

class SettingFragment: Fragment(R.layout.fragment_setting), View.OnClickListener {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var viewModel: SettingViewModel

    override fun onResume() {
        super.onResume()
        checkSentimentMonitor()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        setupViewModel()
        setupObserver()
        setupViewListener()
        checkSentimentMonitor()

        return binding.root
    }

    private fun setupViewModel() {
        val viewModelFactory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[SettingViewModel::class.java]
    }

    private fun setupObserver() {
        viewModel.isFocusModeEnabled.observe(viewLifecycleOwner) {
            binding.swFocusMode.isChecked = it
        }

        viewModel.isCustomLimitStateEnabled.observe(viewLifecycleOwner) {
            binding.swCustomLimit.isChecked = it

            binding.cvCustomLimit.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.customLimitValue.observe(viewLifecycleOwner) {
            binding.edtCustomLimit.setText(it)
        }
    }

    private fun setupViewListener() {
        binding.edtCustomLimit.setOnFocusChangeListener {_, focused ->
            if (focused) showTimePicker()
        }

        binding.swFocusMode.setOnClickListener(this)
        binding.swCustomLimit.setOnClickListener(this)
        binding.swSentimentMonitor.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.btnProfile.setOnClickListener(this)
        binding.btnSentiment.setOnClickListener(this)
    }

    private fun checkSentimentMonitor() {
        val enabled = requireActivity().isNotificationListenerServiceEnabled()
        binding.swSentimentMonitor.isChecked = enabled
        binding.btnSentiment.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(requireActivity(), { _, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                binding.edtCustomLimit.clearFocus()
                viewModel.saveCustomLimitValue(selectedTime)
            },
            7, 0,
            true
        )
        timePickerDialog.show()
    }

    private fun logout() {
        viewModel.clearToken()
        requireActivity().moveToActivity(LoginActivity::class.java)
    }

    override fun onClick(view: View) {
        when (view) {
            binding.swFocusMode -> {
                val isFocusModeEnabled = viewModel.isFocusModeEnabled.value ?: false
                viewModel.saveFocusMode(!isFocusModeEnabled)
            }

            binding.swCustomLimit -> {
                val isCustomLimitStateEnabled = viewModel.isCustomLimitStateEnabled.value ?: false
                viewModel.saveCustomLimitState(!isCustomLimitStateEnabled)
            }

            binding.swSentimentMonitor -> {
                requireActivity().openNotificationAccessSettings()
            }

            binding.btnLogout -> logout()

            binding.btnProfile -> {
                requireActivity().moveToActivity(ProfileActivity::class.java)
            }

            binding.btnSentiment -> {
                requireActivity().moveToActivity(SentimentActivity::class.java)
            }
        }
    }
}