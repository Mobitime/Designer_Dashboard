package com.example.designerdashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.designerdashboard.databinding.ActivityProjectTeamBinding
import com.example.designerdashboard.databinding.ItemTeamMemberBinding

class ProjectTeamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProjectTeamBinding
    private lateinit var teamAdapter: TeamMemberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val projectName = intent.getStringExtra("PROJECT_NAME") ?: "Проект"
        binding.projectTeamTitle.text = "Команда проекта: $projectName"

        setupTeamMembers()
        setupBackButton()
    }

    private fun setupTeamMembers() {
        val teamMembers = when (intent.getStringExtra("PROJECT_NAME")) {
            "Дизайн мобильного приложения" -> listOf(
                TeamMember("Иван Петров", "UX/UI Дизайнер", "Разработка интерфейса", R.drawable.ic_profile),
                TeamMember("Анна Смирнова", "Product Manager", "Управление проектом", R.drawable.ic_profile)
            )
            "Брендинг для компании" -> listOf(
                TeamMember("Елена Козлова", "Арт-директор", "Концепция брендинга", R.drawable.ic_profile),
                TeamMember("Михаил Соколов", "Графический дизайнер", "Разработка логотипа", R.drawable.ic_profile)
            )
            "Веб-сайт для агентства" -> listOf(
                TeamMember("Дмитрий Иванов", "Веб-дизайнер", "Макеты страниц", R.drawable.ic_profile),
                TeamMember("Наталья Попова", "Frontend разработчик", "Верстка и интеграция", R.drawable.ic_profile)
            )
            else -> emptyList()
        }

        teamAdapter = TeamMemberAdapter(teamMembers)
        binding.teamMembersRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProjectTeamActivity)
            adapter = teamAdapter
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}

class TeamMemberAdapter(private val teamMembers: List<TeamMember>) :
    RecyclerView.Adapter<TeamMemberAdapter.TeamMemberViewHolder>() {

    inner class TeamMemberViewHolder(private val binding: ItemTeamMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(teamMember: TeamMember) {
            binding.apply {
                memberName.text = teamMember.name
                memberRole.text = teamMember.role
                memberContribution.text = teamMember.contribution
                memberAvatar.setImageResource(teamMember.avatarResId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberViewHolder {
        val binding = ItemTeamMemberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TeamMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamMemberViewHolder, position: Int) {
        holder.bind(teamMembers[position])
    }

    override fun getItemCount() = teamMembers.size
}