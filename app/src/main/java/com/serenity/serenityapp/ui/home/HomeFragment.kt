package com.serenity.serenityapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.model.Activity
import com.serenity.serenityapp.data.model.Challenge
import com.serenity.serenityapp.data.model.api.response.ChallengeData
import com.serenity.serenityapp.data.model.api.response.ProfileData
import com.serenity.serenityapp.databinding.FragmentHomeBinding
import com.serenity.serenityapp.helper.StartTime
import com.serenity.serenityapp.helper.ViewModelFactory
import com.serenity.serenityapp.helper.getFormatedTimeUsed
import com.serenity.serenityapp.helper.getSocialMediaUsages
import com.serenity.serenityapp.helper.moveToActivity
import com.serenity.serenityapp.ui.adapter.ActivityRecentRecycleViewAdapter
import com.serenity.serenityapp.ui.adapter.ChallengeRecycleViewAdapter
import com.serenity.serenityapp.ui.challenge.ChallengeFragment
import com.serenity.serenityapp.ui.login.LoginActivity
import com.serenity.serenityapp.ui.login.LoginViewModel
import com.serenity.serenityapp.ui.main.MainActivity
import com.serenity.serenityapp.ui.setup.SetupActivity
import com.serenity.serenityapp.ui.usage.UsageFragment

class HomeFragment: Fragment(R.layout.fragment_home), View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupViewModel()
        setupObserver()
        setupOnClickListener()
        showRecentActivityRecycleView()

        return binding.root
    }

    private fun setupViewModel() {
        val viewModelFactory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[HomeViewModel::class.java]
        viewModel.getChallenges()
        viewModel.getProfile()
    }

    private fun setupObserver() {
        viewModel.challenges.observe(viewLifecycleOwner) { challenges ->
            showChallengeRecycleView(challenges)
        }

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            binding.tvUserName.text = profile.name
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.llLoader.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupOnClickListener() {
        binding.tvActivityMoreLabel.setOnClickListener(this)
        binding.tvChallengeMoreLabel.setOnClickListener(this)
    }

    private fun showRecentActivityRecycleView() {
        val activities = requireActivity().getSocialMediaUsages(StartTime.TODAY, 4)
        val totalTimeUsed = activities.sumOf { it.totalTimeUsed }

        val adapter = ActivityRecentRecycleViewAdapter(activities)
        binding.rvActivity.adapter = adapter
        binding.rvActivity.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.tvActivityLabel.text = "Recent Activity (${getFormatedTimeUsed(totalTimeUsed)})"
    }

    private fun showChallengeRecycleView(challenges: List<ChallengeData>) {
        if (challenges.isNotEmpty()) {
            var sortedChallenges = challenges.sortedByDescending { it.createdAt }
            sortedChallenges = sortedChallenges.take(4)

            val adapter = ChallengeRecycleViewAdapter(sortedChallenges)
            binding.rvChallenge.adapter = adapter
            binding.rvChallenge.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            binding.rvChallenge.visibility = View.VISIBLE

            binding.tvNoChallenge.visibility = View.GONE
        } else {
            binding.rvChallenge.visibility = View.GONE
            binding.tvNoChallenge.visibility = View.VISIBLE
        }
    }

    override fun onClick(view: View) {
        val activity = requireActivity() as MainActivity

        when(view) {
            binding.tvActivityMoreLabel -> {
                activity.changeSelectedNavigation(R.id.navigation_activity)
            }

            binding.tvChallengeMoreLabel -> {
                activity.changeSelectedNavigation(R.id.navigation_challenge)
            }
        }
    }
}