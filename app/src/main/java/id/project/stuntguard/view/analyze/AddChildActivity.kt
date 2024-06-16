package id.project.stuntguard.view.analyze

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import id.project.stuntguard.R
import id.project.stuntguard.databinding.ActivityAddChildBinding
import id.project.stuntguard.utils.component.CustomAlertDialog
import id.project.stuntguard.utils.helper.ViewModelFactory

class AddChildActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddChildBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<AnalyzeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val customAlertDialog = CustomAlertDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChildBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val authToken = intent.getStringExtra(EXTRA_TOKEN).toString()

        val genderOptions = resources.getStringArray(R.array.gender)
        val genderOptionsAdapter = ArrayAdapter(this, R.layout.dropdown_item, genderOptions)

        binding.apply {
            genderDropdown.setAdapter(genderOptionsAdapter)
            genderDropdown.setDropDownBackgroundResource(R.color.medium_grey)

            backButton.setOnClickListener {
                // to Remove AddChildActivity and back to AnalyzeFragment :
                finish()
            }

            // to pick image using both Camera or Gallery :
            previewImage.setOnClickListener {
                ImagePicker.with(this@AddChildActivity)
                    .compress(1024) // compress Image Size to 1 mb
                    .start()
            }

            submitButton.setOnClickListener {
                analyze(authToken = authToken)
            }
        }

        viewModel.isLoading.observe(this@AddChildActivity) {
            showLoading(it)
        }

        viewModel.addNewChildResponse.observe(this@AddChildActivity) { response ->
            customAlertDialog.apply {
                create(
                    title = response.status,
                    message = response.message
                ) {
                    finish()
                }
                show()
            }
        }

        viewModel.errorResponse.observe(this@AddChildActivity) { response ->
            customAlertDialog.apply {
                create(
                    title = "Invalid",
                    message = response.message
                ) {
                    // Do Nothing
                }
                show()
            }
        }
    }

    // ImagePicker(Github Dependency) Component :
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                currentImageUri = data?.data!!
                binding.previewImage.setImageURI(currentImageUri)
            }

            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }

            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
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

    private fun analyze(authToken: String) {
        currentImageUri?.let { uri ->
            val name = binding.childNameEditText.text.toString().trim()
            val gender = binding.genderDropdown.text.toString().trim()

            viewModel.addNewChild(
                context = this@AddChildActivity,
                authToken = authToken,
                name = name,
                image = uri,
                gender = gender
            )
        } ?: customAlertDialog.apply {
            create(
                title = "Invalid",
                message = "No Media Selected"
            ) {
                // Do Nothing
            }
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}