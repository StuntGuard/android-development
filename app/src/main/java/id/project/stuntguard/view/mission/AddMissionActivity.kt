package id.project.stuntguard.view.mission

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.project.stuntguard.R
import id.project.stuntguard.data.model.ChildModel
import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.databinding.ActivityAddMissionBinding
import id.project.stuntguard.utils.component.CustomAlertDialog
import id.project.stuntguard.utils.helper.ViewModelFactory

class AddMissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMissionBinding
    private val viewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel by viewModels<MissionViewModel> {
        viewModelFactory
    }
    private var listChild = arrayListOf<ChildModel>()
    private var listChildName = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authToken = intent.getStringExtra(EXTRA_TOKEN).toString()
        viewModel.getAllChild(authToken = authToken)

        setupView()
        setupAction(authToken = authToken)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        viewModel.getAllChildResponse.observe(this@AddMissionActivity) { response ->
            setupChildList(response)
        }

        viewModel.addMissionResponse.observe(this@AddMissionActivity) { response ->
            val customAlertDialog = CustomAlertDialog(this@AddMissionActivity)
            customAlertDialog.create(
                title = response.status,
                message = response.message
            ) {
                finish()
            }
            customAlertDialog.show()
        }

        viewModel.errorResponse.observe(this@AddMissionActivity) { errorMessage ->
            val customAlertDialog = CustomAlertDialog(this@AddMissionActivity)
            customAlertDialog.create(
                title = "Invalid",
                message = errorMessage.toString()
            ) {

            }
            customAlertDialog.show()
        }

        viewModel.isLoading.observe(this@AddMissionActivity) {
            showLoading(it)
        }
    }

    private fun setupAction(authToken: String) {
        binding.apply {
            backButton.setOnClickListener {
                // to Remove AddMissionActivity and back to MissionFragment :
                finish()
            }
            submitButton.setOnClickListener {
                addMission(authToken)
            }
        }
    }

    private fun setupChildList(response: GetAllChildResponse) {
        listChild.clear()
        listChildName.clear()

        for (child in response.data) {
            val childData = ChildModel(
                id = child.id,
                name = child.name,
                urlPhoto = child.urlPhoto,
                gender = child.gender
            )
            listChild.add(childData)
            listChildName.add(child.name)
        }

        val childNameOptions = listChildName
        val childOptionsAdapter =
            ArrayAdapter(this@AddMissionActivity, R.layout.dropdown_item, childNameOptions)
        binding.childNameDropdown.apply {
            setAdapter(childOptionsAdapter)
            setDropDownBackgroundResource(R.color.medium_grey)
        }
    }

    private fun addMission(authToken: String) {
        val childName = binding.childNameDropdown.text.toString().trim()
        val title = binding.titleMissionEditText.text.toString().trim()
        val description = binding.descriptionMissionEditText.text.toString().trim()
        var childData: ChildModel? = null

        for (child in listChild) {
            if (childName == child.name) {
                childData = child
            }
        }

        if (childData != null) {
            viewModel.addMission(
                authToken = authToken,
                idChild = childData.id,
                title = title,
                description = description
            )

        } else {
            val customAlertDialog = CustomAlertDialog(this@AddMissionActivity)
            customAlertDialog.create(
                title = "Invalid",
                message = "No Child Selected"
            ) {
                // Do nothing
            }
            customAlertDialog.show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}