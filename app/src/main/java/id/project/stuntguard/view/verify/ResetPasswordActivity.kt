package id.project.stuntguard.view.verify

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.project.stuntguard.databinding.ActivityResetPasswordBinding
import id.project.stuntguard.utils.component.CustomAlertDialog
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.view.login.LoginActivity

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private val viewModel by viewModels<VerifyViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val customAlertDialog = CustomAlertDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = intent.getStringExtra(EXTRA_TOKEN).toString()

        setupView()
        setupAction(token = token)
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

        viewModel.resetPasswordResponse.observe(this@ResetPasswordActivity) { response ->
            customAlertDialog.apply {
                create(
                    title = response.status,
                    message = response.message
                ) {
                    val intentToLogin =
                        Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                    startActivity(intentToLogin)
                    finish()
                }
                show()
            }
        }
    }

    private fun setupAction(token: String) {
        binding.apply {
            backButton.setOnClickListener {
                finish()
            }
            submitButton.setOnClickListener {
                binding.apply {
                    val newPassword = newPasswordEditText.text.toString().trim()
                    val confirmPassword = confirmPasswordEditText.text.toString().trim()

                    if (newPassword != confirmPassword) {
                        customAlertDialog.apply {
                            create(
                                title = "Invalid",
                                message = "New and Confirm password don't match"
                            ) {
                                // Do Nothing
                            }
                            show()
                        }

                    } else {
                        viewModel.updatePassword(token = token, password = newPassword)

                        // Don't update the password on when try on login screen
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}