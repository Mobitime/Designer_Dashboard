package com.example.designerdashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.designerdashboard.databinding.ActivityProjectDetailBinding

class ProjectDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProjectDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val projectName = intent.getStringExtra("PROJECT_NAME") ?: "Проект"
        val projectPrice = intent.getIntExtra("PROJECT_PRICE", 0)

        setupViews(projectName, projectPrice)
        setupBackButton()
    }

    private fun setupViews(projectName: String, projectPrice: Int) {
        binding.apply {
            projectNameDetail.text = projectName
            projectPriceDetail.text = "$projectPrice ₽"
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}