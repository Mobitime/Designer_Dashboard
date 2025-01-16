package com.example.designerdashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.designerdashboard.databinding.ActivityProfileBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            selectedImageUri?.let {
                binding.profileImage.setImageURI(it)
                saveProfileImage(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadProfileData()
        setupButtons()
    }

    private fun setupButtons() {
        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            btnEditProfile.setOnClickListener {
                showEditProfileDialog()
            }

            profileImage.setOnClickListener {
                val pickIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                pickImageLauncher.launch(pickIntent)
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showEditProfileDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.editName)
        val emailEditText = dialogView.findViewById<EditText>(R.id.editEmail)

        nameEditText.setText(binding.profileName.text)
        emailEditText.setText(binding.profileEmail.text)

        AlertDialog.Builder(this)
            .setTitle("Редактировать профиль")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = nameEditText.text.toString()
                val newEmail = emailEditText.text.toString()

                binding.profileName.text = newName
                binding.profileEmail.text = newEmail

                saveProfileData(newName, newEmail)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun saveProfileData(name: String, email: String) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("PROFILE_NAME", name)
            putString("PROFILE_EMAIL", email)
            apply()
        }
    }

    private fun loadProfileData() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val savedName = sharedPref.getString("PROFILE_NAME", "Иван Петров")
        val savedEmail = sharedPref.getString("PROFILE_EMAIL", "ivan.petrov@example.com")

        binding.profileName.text = savedName
        binding.profileEmail.text = savedEmail

        loadProfileImage()
    }

    private fun saveProfileImage(imageUri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val file = File(filesDir, "profile_image.jpg")

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("PROFILE_IMAGE_PATH", file.absolutePath)
                apply()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadProfileImage() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val imagePath = sharedPref.getString("PROFILE_IMAGE_PATH", null)

        imagePath?.let {
            val file = File(it)
            if (file.exists()) {
                binding.profileImage.setImageURI(Uri.fromFile(file))
            }
        }
    }
}