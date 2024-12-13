package com.serenity.serenityapp.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.model.api.response.ProfileData
import com.serenity.serenityapp.databinding.ActivityProfileBinding
import com.serenity.serenityapp.helper.ViewModelFactory
import com.serenity.serenityapp.helper.compressImage
import com.serenity.serenityapp.helper.moveToActivity
import com.serenity.serenityapp.ui.challenge.ChallengeViewModel
import com.serenity.serenityapp.ui.main.MainActivity
import java.util.Locale

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel

    private val PICK_IMAGE_REQUEST_CODE = 1

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupObserver()
        setupViewListener()
    }

    private fun setupViewModel() {
        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    private fun setupObserver() {
        viewModel.profile.observe(this) { profile ->
            showProfile(profile)
        }

        viewModel.loading.observe(this) { loading ->
            binding.llLoader.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isEditSuccess.observe(this) { isEditSuccess ->
            if (isEditSuccess) {
                Toast.makeText(this, "Profile successfully updated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupViewListener() {
        binding.btnEditProfile.setOnClickListener(this)
        binding.civPhoto.setOnClickListener(this)
    }

    private fun showProfile(profile: ProfileData) {
        binding.edtName.setText(profile.name)
        binding.edtAge.setText(profile.age.toString())
        binding.edtEmail.setText(profile.email)

        val genders = resources.getStringArray(R.array.gender_values)
        val gender = genders.first { it.lowercase() == profile.gender.lowercase() }
        val genderIndex = genders.indexOf(gender)

        binding.edtGender.setSelection(genderIndex)

        val uiAvatarUrl = "https://ui-avatars.com/api/?background=00BFA6&size=128&name=${profile.name}"
        val photoUrl = profile.photo_profile ?: uiAvatarUrl

        Glide.with(this)
            .load(photoUrl)
            .into(binding.civPhoto)
    }

    private fun editProfile() {
        val name = binding.edtName.text.toString()
        val age = binding.edtAge.text.toString().toInt()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        val genderIndex = binding.edtGender.selectedItemPosition
        val genders = resources.getStringArray(R.array.gender_values)
        val gender = genders[genderIndex]

        val profile = ProfileData(
            null,
            name,
            email,
            password,
            age,
            gender,
            null
        )

        val photo = this.compressImage(imageUri)

        viewModel.editProfile(profile, photo)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    private fun previewImage() {
        binding.civPhoto.setImageURI(imageUri)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveToActivity(MainActivity::class.java) {
            putExtra("menu", R.id.navigation_setting)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            PICK_IMAGE_REQUEST_CODE -> {
                imageUri = data?.data as Uri
                previewImage()
            }
        }
    }

    override fun onClick(view: View) {
        when(view) {
            binding.btnEditProfile -> editProfile()

            binding.civPhoto -> openGallery()
        }
    }
}