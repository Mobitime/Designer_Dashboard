package com.example.designerdashboard

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.designerdashboard.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButton()
        setupThemeSwitch()
        setupNotificationSwitch()
    }

    private fun applyTheme() {
        val isDarkMode = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
            .getBoolean("dark_mode", false)
        
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener { 
            onBackPressedDispatcher.onBackPressed() 
        }
    }

    private fun setupThemeSwitch() {
        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        
        binding.switchDarkMode.isChecked = isDarkMode
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
            
            val mode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
            
            recreate()
        }
    }

    private fun setupNotificationSwitch() {
        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val notificationsEnabled = sharedPreferences.getBoolean("notifications", true)
        
        binding.switchNotifications.isChecked = notificationsEnabled
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("notifications", isChecked).apply()
        }
    }
}