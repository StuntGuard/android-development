package id.project.stuntguard.view.verify

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.project.stuntguard.databinding.ActivityVerifyCodeBinding
import id.project.stuntguard.utils.component.CustomAlertDialog
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.view.login.LoginActivity

class VerifyCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyCodeBinding
    private val viewModel by viewModels<VerifyViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val customAlertDialog = CustomAlertDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val identifier = intent.getStringExtra(EXTRA_IDENTIFIER).toString()
        val email = intent.getStringExtra(EXTRA_EMAIL).toString()
        val name = intent.getStringExtra(EXTRA_NAME).toString()
        val password = intent.getStringExtra(EXTRA_PASSWORD).toString()

        setupView()
        setupAction(identifier = identifier, email = email, name = name, password = password)
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

        viewModel.signUpResponse.observe(this@VerifyCodeActivity) { response ->
            customAlertDialog.apply {
                create(
                    title = response.status,
                    message = response.message
                ) {
                    val intentToSignIn = Intent(this@VerifyCodeActivity, LoginActivity::class.java)
                    startActivity(intentToSignIn)
                    finish()
                }
                show()
            }
        }

        viewModel.isLoading.observe(this@VerifyCodeActivity) {
            showLoading(it)
        }
    }

    private fun setupAction(identifier: String, email: String, name: String, password: String) {
        binding.apply {
            backButton.setOnClickListener {
                finish()
            }

            verificationButton.setOnClickListener {
                val token = codeEditText.text.toString().trim()
                if (identifier == "New") {
                    viewModel.verifyNewEmail(token = token)

                    viewModel.verifyNewEmailResponse.observe(this@VerifyCodeActivity) { response ->
                        customAlertDialog.apply {
                            create(
                                title = response.status,
                                message = response.message
                            ) {
                                viewModel.signUp(name = name, email = email, password = password)
                            }
                            show()
                        }
                    }

                } else {
                    viewModel.verifyEmail(token = token)

                    viewModel.verifyEmailResponse.observe(this@VerifyCodeActivity) { response ->
                        customAlertDialog.apply {
                            create(
                                title = response.status,
                                message = response.message
                            ) {
                                val intentToUpdatePassword =
                                    Intent(
                                        this@VerifyCodeActivity,
                                        ResetPasswordActivity::class.java
                                    )
                                intentToUpdatePassword.putExtra(
                                    ResetPasswordActivity.EXTRA_TOKEN,
                                    token
                                )
                                startActivity(intentToUpdatePassword)
                                finish()
                            }
                            show()
                        }
                    }
                }
            }

            resendCodeButton.setOnClickListener {
                if (identifier == "New") {
                    viewModel.newEmailCheck(email)

                } else {
                    viewModel.checkEmail(email)
                }

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

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        const val EXTRA_IDENTIFIER = "extra_identifier"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PASSWORD = "extra_password"
    }
}