package com.example.designerdashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.designerdashboard.databinding.ActivityPriceListBinding
import com.example.designerdashboard.databinding.ItemPriceBinding

data class PriceItem(
    val name: String,
    val description: String,
    val price: Int,
    val imageResId: Int? = null
)

class PriceListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPriceListBinding
    private lateinit var priceAdapter: PriceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPriceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButton()
        setupPriceList()
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener { 
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupPriceList() {
        val projects = listOf(
            PriceItem(
                "Дизайн мобильного приложения", 
                "UX/UI разработка", 
                50000
            ),
            PriceItem(
                "Брендинг для компании", 
                "Фирменный стиль", 
                35000
            ),
            PriceItem(
                "Веб-сайт для агентства", 
                "Разработка сайта", 
                75000
            )
        )

        priceAdapter = PriceAdapter(projects) { project ->

            val detailIntent = Intent(this, ProjectDetailActivity::class.java).apply {
                putExtra("PROJECT_NAME", project.name)
                putExtra("PROJECT_PRICE", project.price)
            }
            startActivity(detailIntent)
        }

        binding.priceRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@PriceListActivity)
            adapter = priceAdapter
        }
    }
}

class PriceAdapter(
    private val projects: List<PriceItem>,
    private val onItemClickListener: (PriceItem) -> Unit
) : RecyclerView.Adapter<PriceAdapter.PriceViewHolder>() {

    inner class PriceViewHolder(private val binding: ItemPriceBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(project: PriceItem) {
            binding.apply {
                projectName.text = project.name
                projectDescription.text = project.description
                projectPrice.text = "${project.price} ₽"
                



                root.setOnClickListener { 
                    onItemClickListener(project)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val binding = ItemPriceBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return PriceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        holder.bind(projects[position])
    }

    override fun getItemCount() = projects.size
}