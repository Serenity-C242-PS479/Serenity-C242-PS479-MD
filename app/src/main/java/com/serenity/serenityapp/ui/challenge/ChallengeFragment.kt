package com.serenity.serenityapp.ui.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.model.api.response.ChallengeData
import com.serenity.serenityapp.databinding.FragmentChallengeBinding
import com.serenity.serenityapp.helper.ViewModelFactory
import com.serenity.serenityapp.helper.moveToActivity
import com.serenity.serenityapp.ui.adapter.ChallengeRecycleViewAdapter

class ChallengeFragment: Fragment(R.layout.fragment_challenge), View.OnClickListener {
    private lateinit var binding: FragmentChallengeBinding
    private lateinit var viewModel: ChallengeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChallengeBinding.inflate(inflater, container, false)

        setupViewModel()
        setupObserver()
        setupViewListener()

        return binding.root
    }

    private fun setupViewListener() {
        binding.btnAdd.setOnClickListener(this)
    }

    private fun setupViewModel() {
        val viewModelFactory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[ChallengeViewModel::class.java]
    }

    private fun setupObserver() {
        viewModel.challenges.observe(viewLifecycleOwner) { challenges ->
            showChallengeRecycleView(challenges.sortedByDescending { it.createdAt })
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.llLoader.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isDeleteSuccess.observe(viewLifecycleOwner) { isDeleteSuccess ->
            if (isDeleteSuccess) {
                Toast.makeText(requireActivity(), "Challenge successfully deleted", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getChallenges()
    }

    private fun showChallengeRecycleView(challenges: List<ChallengeData>) {
        if (challenges.isNotEmpty()) {
            val adapter = ChallengeRecycleViewAdapter(challenges, true) { challenge ->
                viewModel.deleteChallenge(challenge.id)
            }

            binding.rvChallenge.adapter = adapter
            binding.rvChallenge.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            binding.rvChallenge.isNestedScrollingEnabled = false
            binding.rvChallenge.visibility = View.VISIBLE

            binding.tvNoChallenge.visibility = View.GONE
        } else {
            binding.rvChallenge.visibility = View.GONE
            binding.tvNoChallenge.visibility = View.VISIBLE
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnAdd -> {
                requireActivity().moveToActivity(CreateChallengeActivity::class.java)
            }
        }
    }
}