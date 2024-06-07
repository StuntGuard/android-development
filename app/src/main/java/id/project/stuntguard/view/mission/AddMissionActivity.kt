package id.project.stuntguard.view.mission

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import id.project.stuntguard.R
import id.project.stuntguard.databinding.ActivityAddMissionBinding
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.view.analyze.AddChildActivity
import id.project.stuntguard.view.analyze.AnalyzeViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

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

        val authToken = intent.getStringExtra(EXTRA_TOKEN).toString()

        binding.apply {

            btnSubmit.setOnClickListener {
                if(etMissionDesc.text.toString().isEmpty()){
                    showToast("Mission description still empty!")
                    return@setOnClickListener
                } else if (etMissionTitle.text.toString().isEmpty()){
                    showToast("Mission title still empty!")
                    return@setOnClickListener
                }
                postMission()
            }

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun postMission() {
        val title = binding.etMissionTitle.text.toString()
        val description = binding.etMissionDesc.text.toString()

//        val requestBodyTitle = title.toRequestBody("text/plain".toMediaType())
//        val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())

        viewModel.postMission(title,description)
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}