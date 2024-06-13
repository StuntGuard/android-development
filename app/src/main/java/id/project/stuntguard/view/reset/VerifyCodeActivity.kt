package id.project.stuntguard.view.reset

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.project.stuntguard.databinding.ActivityVerifyCodeBinding
import id.project.stuntguard.utils.component.CustomAlertDialog
import id.project.stuntguard.utils.helper.ViewModelFactory

class VerifyCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyCodeBinding
    private val viewModel by viewModels<ResetViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val customAlertDialog = CustomAlertDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra(EXTRA_EMAIL).toString()

        setupView()
        setupAction(email)
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

        viewModel.errorResponse.observe(this@VerifyCodeActivity) { response ->
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
    }

    private fun setupAction(email: String) {
        binding.apply {
            backButton.setOnClickListener {
                finish()
            }

            verificationButton.setOnClickListener {
                val token = codeEditText.text.toString().trim()
                viewModel.verifyEmail(token)

                viewModel.verifyResponse.observe(this@VerifyCodeActivity) { response ->
                    customAlertDialog.apply {
                        create(
                            title = response.status,
                            message = response.message
                        ) {
                            val intentToUpdatePassword =
                                Intent(this@VerifyCodeActivity, ResetPasswordActivity::class.java)
                            intentToUpdatePassword.putExtra(ResetPasswordActivity.EXTRA_TOKEN, token)
                            startActivity(intentToUpdatePassword)
                            finish()
                        }
                        show()
                    }
                }
            }

            resendCodeButton.setOnClickListener {
                viewModel.checkEmail(email)

                customAlertDialog.apply {
                    create(
                        title = "Success",
                        message = "New Code has been sent to your email [ $email ]"
                    ) {
                        // Do Nothing
                    }
                    show()
                }
            }
        }
    }

    companion object {
        const val EXTRA_EMAIL = "extra_email"
    }
}