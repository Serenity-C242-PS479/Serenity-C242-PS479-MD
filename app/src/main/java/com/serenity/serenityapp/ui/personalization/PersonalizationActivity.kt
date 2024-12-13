package com.serenity.serenityapp.ui.personalization

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.serenity.serenityapp.R
import com.serenity.serenityapp.databinding.ActivityPersonalizationBinding
import com.serenity.serenityapp.ui.main.MainActivity
import com.serenity.serenityapp.ui.register.RegisterActivity

class PersonalizationActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPersonalizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOnClickListener()
    }

    private fun setupOnClickListener() {
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.btnSubmit -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}