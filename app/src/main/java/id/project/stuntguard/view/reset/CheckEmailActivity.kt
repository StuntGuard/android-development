package id.project.stuntguard.view.reset

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.project.stuntguard.databinding.ActivityCheckEmailBinding
import id.project.stuntguard.utils.component.CustomAlertDialog
import id.project.stuntguard.utils.helper.ViewModelFactory

class CheckEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckEmailBinding
    private lateinit var emailIntent: String
    private val viewModel by viewModels<ResetViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val customAlertDialog = CustomAlertDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
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

        viewModel.checkEmailResponse.observe(this@CheckEmailActivity) { response ->
            customAlertDialog.apply {
                create(
                    title = response.status,
                    message = response.message
                ) {
                    val intentToVerify = Intent(this@CheckEmailActivity, VerifyCodeActivity::class.java)
                    intentToVerify.putExtra(VerifyCodeActivity.EXTRA_EMAIL, emailIntent)
                    startActivity(intentToVerify)
                }
                show()
            }
        }

        viewModel.errorResponse.observe(this@CheckEmailActivity) { response ->
            customAlertDialog.apply {
                create(
                    title = response.status,
                    message = response.message
                ) {
                    // Do Nothing
                }
                show()
            }
        }

        viewModel.isLoading.observe(this@CheckEmailActivity) {
            showLoading(it)
        }
    }

    private fun setupAction() {
        binding.apply {
            backButton.setOnClickListener {
                finish()
            }

            submitButton.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                emailIntent = email
                viewModel.checkEmail(email)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }
}