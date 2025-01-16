package com.example.designerdashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.designerdashboard.databinding.ActivityMainBinding
import com.example.designerdashboard.databinding.ItemDashboardCardBinding

data class DashboardItem(
    val title: String,
    val icon: Int,
    val description: String,
    val destinationActivity: Class<*>? = null
)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dashboardAdapter: DashboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDashboardRecyclerView()
    }

    private fun setupDashboardRecyclerView() {
        val dashboardItems = listOf(
            DashboardItem(
                "Профиль",
                R.drawable.ic_profile,
                "Управление профилем",
                ProfileActivity::class.java
            ),
            DashboardItem(
                "Проекты",
                R.drawable.ic_projects,
                "Ваши дизайн-проекты",
                ProjectsActivity::class.java
            ),
            DashboardItem(
                "Статистика",
                R.drawable.ic_statistics,
                "Аналитика работы",
                StatisticsActivity::class.java
            ),
            DashboardItem(
                "Настройки",
                R.drawable.ic_settings,
                "Параметры приложения",
                SettingsActivity::class.java
            )
        )

        dashboardAdapter = DashboardAdapter(dashboardItems) { item ->
            when (item.destinationActivity) {
                ProfileActivity::class.java -> {
                    val profileIntent = Intent(this, ProfileActivity::class.java).apply {
                        putExtra("FROM_DASHBOARD", true)
                    }
                    startActivity(profileIntent)
                }
                ProjectsActivity::class.java -> {
                    val projectsIntent = Intent(this, ProjectsActivity::class.java).apply {
                        putExtra("FROM_DASHBOARD", true)
                    }
                    startActivity(projectsIntent)
                }
                else -> {
                    startActivity(Intent(this, item.destinationActivity!!))
                }
            }
        }

        binding.dashboardRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = dashboardAdapter
            addItemDecoration(GridSpacingItemDecoration(2, 32, true))
        }
    }
}

class DashboardAdapter(
    private val items: List<DashboardItem>,
    private val onItemClick: (DashboardItem) -> Unit
) : RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {

    inner class DashboardViewHolder(val binding: ItemDashboardCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DashboardItem) {
            binding.cardTitle.text = item.title
            binding.cardIcon.setImageResource(item.icon)
            binding.cardDescription.text = item.description

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val binding = ItemDashboardCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DashboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
