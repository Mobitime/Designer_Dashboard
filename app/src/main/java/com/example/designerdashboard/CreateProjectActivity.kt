package com.example.designerdashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.designerdashboard.databinding.ActivityCreateProjectBinding
import com.example.designerdashboard.databinding.ItemTeamMemberSelectionBinding

class CreateProjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProjectBinding
    private lateinit var teamMembersAdapter: TeamMembersSelectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButton()
        setupTeamMembersRecyclerView()
        setupCreateProjectButton()
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupTeamMembersRecyclerView() {
        val allTeamMembers = listOf(
            TeamMember("Иван Петров", "UX/UI Дизайнер", "Senior", R.drawable.ic_profile),
            TeamMember("Анна Смирнова", "Product Manager", "Middle", R.drawable.ic_profile),
            TeamMember("Елена Козлова", "Арт-директор", "Senior", R.drawable.ic_profile),
            TeamMember("Михаил Соколов", "Графический дизайнер", "Middle", R.drawable.ic_profile),
            TeamMember("Дмитрий Иванов", "Веб-дизайнер", "Junior", R.drawable.ic_profile),
            TeamMember("Наталья Попова", "Frontend разработчик", "Middle", R.drawable.ic_profile)
        )

        teamMembersAdapter = TeamMembersSelectionAdapter(allTeamMembers)
        binding.teamMembersRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CreateProjectActivity)
            adapter = teamMembersAdapter
        }
    }

    private fun setupCreateProjectButton() {
        binding.btnCreateProject.setOnClickListener {
            val projectName = binding.projectNameInput.text.toString()
            val projectDescription = binding.projectDescriptionInput.text.toString()
            val selectedMembers = teamMembersAdapter.getSelectedMembers()

            if (projectName.isNotBlank() && projectDescription.isNotBlank() && selectedMembers.isNotEmpty()) {
                val newProject = Project(
                    name = projectName,
                    description = projectDescription,
                    status = "Планируется",
                    imageResId = R.drawable.project_placeholder
                )

                // TODO: Сохранение проекта в базу данных или SharedPreferences

                val resultIntent = Intent().apply {
                    putExtra("NEW_PROJECT", newProject)
                    putParcelableArrayListExtra("PROJECT_TEAM", selectedMembers.map { it as Parcelable }.toCollection(ArrayList()))
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class TeamMembersSelectionAdapter(
    private val teamMembers: List<TeamMember>
) : RecyclerView.Adapter<TeamMembersSelectionAdapter.TeamMemberViewHolder>() {

    private val selectedMembers = mutableSetOf<TeamMember>()

    inner class TeamMemberViewHolder(
        private val binding: ItemTeamMemberSelectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(teamMember: TeamMember) {
            binding.apply {
                memberName.text = teamMember.name
                memberRole.text = teamMember.role
                memberAvatar.setImageResource(teamMember.avatarResId)

                checkBoxSelectMember.isChecked = teamMember in selectedMembers
                checkBoxSelectMember.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedMembers.add(teamMember)
                    } else {
                        selectedMembers.remove(teamMember)
                    }
                }
            }
        }
    }

    fun getSelectedMembers(): List<TeamMember> = selectedMembers.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberViewHolder {
        val binding = ItemTeamMemberSelectionBinding.inflate(
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