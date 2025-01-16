package com.example.designerdashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.designerdashboard.databinding.ActivityProjectsBinding
import com.example.designerdashboard.databinding.ItemProjectBinding

@Parcelize
data class Project(
    val name: String,
    val description: String,
    val status: String,
    val imageResId: Int
) : Parcelable

@Parcelize
data class TeamMember(
    val name: String,
    val role: String,
    val contribution: String,
    val avatarResId: Int
) : Parcelable

class ProjectsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProjectsBinding
    private lateinit var projectAdapter: ProjectAdapter
    private val createProjectLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newProject = result.data?.getParcelableExtra<Project>("NEW_PROJECT")
            val projectTeam = result.data?.getParcelableArrayListExtra<TeamMember>("PROJECT_TEAM")

            newProject?.let { 

                (projectAdapter.projects as MutableList).add(newProject)
                projectAdapter.notifyItemInserted(projectAdapter.itemCount - 1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fromDashboard = intent.getBooleanExtra("FROM_DASHBOARD", false)
        setupRecyclerView()
        setupBackButton()
        setupCreateProjectButton()
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener { 
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupCreateProjectButton() {
        binding.btnCreateProject.setOnClickListener {
            val createProjectIntent = Intent(this, CreateProjectActivity::class.java)
            createProjectLauncher.launch(createProjectIntent)
        }
    }

    private fun setupRecyclerView() {
        val projects = mutableListOf(
            Project("Дизайн мобильного приложения", 
                    "Разработка UX/UI для стартапа", 
                    "В работе", 
                    R.drawable.project_mobile_app),
            Project("Брендинг для компании", 
                    "Создание фирменного стиля", 
                    "Завершен", 
                    R.drawable.project_branding),
            Project("Веб-сайт для агентства", 
                    "Разработка современного сайта", 
                    "Планируется", 
                    R.drawable.project_website)
        )

        projectAdapter = ProjectAdapter(projects) { project ->
            val teamIntent = Intent(this, ProjectTeamActivity::class.java).apply {
                putExtra("PROJECT_NAME", project.name)
            }
            startActivity(teamIntent)
        }

        binding.projectsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProjectsActivity)
            adapter = projectAdapter
        }
    }
}

class ProjectAdapter(
    val projects: List<Project>,
    private val onProjectClickListener: (Project) -> Unit
) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    inner class ProjectViewHolder(private val binding: ItemProjectBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(project: Project) {
            binding.apply {
                projectName.text = project.name
                projectDescription.text = project.description
                projectStatus.text = project.status
                projectImage.setImageResource(project.imageResId)

                root.setOnClickListener { 
                    onProjectClickListener(project)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ItemProjectBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(projects[position])
    }

    override fun getItemCount() = projects.size
}