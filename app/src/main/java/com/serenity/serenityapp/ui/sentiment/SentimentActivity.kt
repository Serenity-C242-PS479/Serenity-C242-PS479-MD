package com.serenity.serenityapp.ui.sentiment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.model.api.response.ChallengeData
import com.serenity.serenityapp.data.model.database.UserWithSentiment
import com.serenity.serenityapp.databinding.ActivitySentimentBinding
import com.serenity.serenityapp.helper.ViewModelFactory
import com.serenity.serenityapp.helper.moveToActivity
import com.serenity.serenityapp.ui.adapter.ChallengeRecycleViewAdapter
import com.serenity.serenityapp.ui.adapter.SentimentRecycleViewAdapter
import com.serenity.serenityapp.ui.challenge.ChallengeViewModel
import com.serenity.serenityapp.ui.main.MainActivity

class SentimentActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySentimentBinding
    private lateinit var viewModel: SentimentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySentimentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupObserver()
    }

    private fun setupViewModel() {
        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[SentimentViewModel::class.java]
    }

    private fun setupObserver() {
        viewModel.usersWithSentiments.observe(this) { usersWithSentiments ->
            showSentimentRecycleView(usersWithSentiments)
        }
    }

    private fun showSentimentRecycleView(usersWithSentiment: List<UserWithSentiment>) {
        if (usersWithSentiment.isNotEmpty()) {
            val adapter = SentimentRecycleViewAdapter(usersWithSentiment)
            binding.rvChallenge.adapter = adapter
            binding.rvChallenge.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvChallenge.isNestedScrollingEnabled = false
            binding.rvChallenge.visibility = View.VISIBLE

            binding.tvNoSentiment.visibility = View.GONE
        } else {
            binding.rvChallenge.visibility = View.GONE
            binding.tvNoSentiment.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveToActivity(MainActivity::class.java) {
            putExtra("menu", R.id.navigation_setting)
        }
    }
}