package id.project.stuntguard.view.mission

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import id.project.stuntguard.databinding.ActivityAddMissionBinding
import id.project.stuntguard.utils.helper.ViewModelFactory

class AddMissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMissionBinding
    private val viewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel by viewModels<AddMissionViewModel> {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val authToken = intent.getStringExtra(EXTRA_TOKEN).toString()
//        val idChild = intent.getStringExtra(EXTRA_ID).toString().toInt()
        val idChild = 27

        binding.apply {

            backButton.setOnClickListener {
                // to Remove AddChildActivity and back to AnalyzeFragment :
                finish()
            }

            btnSubmit.setOnClickListener {
                postMissions(authToken = authToken, idChild = idChild)
            }
        }

        viewModel.addMissionResponse.observe(this@AddMissionActivity) { response ->
            AlertDialog.Builder(this).apply {
                setTitle("Success")
                setMessage(response.message)
                setPositiveButton("Ok") { _, _ ->
                    finish()
                }
                create()
                show()
            }
        }

        viewModel.errorResponse.observe(this@AddMissionActivity) { errorMessage ->
            AlertDialog.Builder(this).apply {
                setTitle("Invalid")
                setMessage(errorMessage)
                setPositiveButton("Ok") { _, _ ->
                    // Do Nothing
                }
                create()
                show()
            }
        }
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
    }

    private fun postMissions(authToken: String, idChild: Int) {
        val title = binding.etMissionTitle.text.toString().trim()
        val description = binding.etMissionDesc.text.toString().trim()

        viewModel.postMissions(
            context = this@AddMissionActivity,
            authToken = authToken,
            idChild = idChild,
            title = title,
            description = description
        )
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
//        const val EXTRA_ID = "extra_id"
    }
}