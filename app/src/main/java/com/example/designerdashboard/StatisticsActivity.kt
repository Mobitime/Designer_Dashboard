package com.example.designerdashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.designerdashboard.databinding.ActivityStatisticsBinding
import com.example.designerdashboard.databinding.ItemStatisticBinding

data class StatisticItem(
    val label: String,
    val value: String
)

class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatisticsBinding
    private lateinit var statisticsAdapter: StatisticsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButton()
        setupOverallStats()
        setupStatisticsRecyclerView()
        setupPriceListButton()
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupOverallStats() {
        binding.apply {
            totalProjectsValue.text = "15"

        }
    }

    private fun setupStatisticsRecyclerView() {
        val statisticsList = listOf(
            StatisticItem("Часы работы", "120 часов"),
            StatisticItem("Средняя длительность проекта", "2 недели"),
            StatisticItem("Доход", "500 000 ₽"),
            StatisticItem("Клиенты", "12 компаний")
        )

        statisticsAdapter = StatisticsAdapter(statisticsList)
        binding.statisticsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@StatisticsActivity)
            adapter = statisticsAdapter
        }
    }

    private fun setupPriceListButton() {
        binding.btnPriceList.setOnClickListener {
            val priceListIntent = Intent(this, PriceListActivity::class.java)
            startActivity(priceListIntent)
        }
    }
}

class StatisticsAdapter(
    private val statisticsList: List<StatisticItem>
) : RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>() {

    inner class StatisticsViewHolder(
        private val binding: ItemStatisticBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(statisticItem: StatisticItem) {
            binding.apply {
                statisticLabel.text = statisticItem.label
                statisticValue.text = statisticItem.value
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        val binding = ItemStatisticBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return StatisticsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        holder.bind(statisticsList[position])
    }

    override fun getItemCount() = statisticsList.size
}
