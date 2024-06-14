package id.project.stuntguard.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.project.stuntguard.databinding.ActivitySignupBinding
import id.project.stuntguard.utils.component.CustomAlertDialog
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.view.login.LoginActivity
import id.project.stuntguard.view.verify.VerifyCodeActivity
import id.project.stuntguard.view.verify.VerifyViewModel

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<VerifyViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val customAlertDialog = CustomAlertDialog(this@SignupActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
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

        viewModel.isLoading.observe(this@SignupActivity) {
            showLoading(it)
        }

        viewModel.errorResponse.observe(this@SignupActivity) { response ->
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

    private fun setupAction() {
        binding.signUpButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            val hasUpperCase = Regex(".*[A-Z].*")
            val hasSymbol = Regex(".*[!@#\$%^&*(),.?\":{}|<>].*")
            val hasNumber = Regex(".*[0-9].*")

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                customAlertDialog.apply {
                    create(
                        title = "Invalid",
                        message = "Name, Email or Password can't be empty"
                    ) {
                        // Do Nothing
                    }
                    show()
                }

            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                customAlertDialog.apply {
                    create(
                        title = "Invalid",
                        message = "Invalid Email Address"
                    ) {
                        // Do Nothing
                    }
                    show()
                }

            } else if (
                password.length < 8 ||
                !hasUpperCase.containsMatchIn(password) ||
                !hasSymbol.containsMatchIn(password) ||
                !hasNumber.containsMatchIn(password)
            ) {
                customAlertDialog.apply {
                    create(
                        title = "Invalid",
                        message = "Password must have at least 8 character, has Capitalize, Symbol and number"
                    ) {
                        // Do Nothing
                    }
                    show()
                }

            } else {
                viewModel.newEmailCheck(email = email)

                viewModel.newEmailCheckResponse.observe(this@SignupActivity) { response ->
                    customAlertDialog.apply {
                        create(
                            title = response.status,
                            message = "${response.message} to [ $email ]"
                        ) {
                            val intentToVerify =
                                Intent(this@SignupActivity, VerifyCodeActivity::class.java)
                            intentToVerify.putExtra(VerifyCodeActivity.EXTRA_IDENTIFIER, "New")
                            intentToVerify.putExtra(VerifyCodeActivity.EXTRA_EMAIL, email)
                            intentToVerify.putExtra(VerifyCodeActivity.EXTRA_NAME, name)
                            intentToVerify.putExtra(VerifyCodeActivity.EXTRA_PASSWORD, password)
                            startActivity(intentToVerify)
                            finish()
                        }
                        show()
                    }
                }
            }
        }

        binding.signInClickable.setOnClickListener {
            val intentToSignIn = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intentToSignIn)
            finish()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.signupImage, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.loginTitle, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameInputField =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailInputField =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordInputField =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signInButton =
            ObjectAnimator.ofFloat(binding.signInClickable, View.ALPHA, 1f).setDuration(100)
        val alreadyHaveAcc =
            ObjectAnimator.ofFloat(binding.alreadyHaveAccount, View.ALPHA, 1f).setDuration(100)
        val signUpButton =
            ObjectAnimator.ofFloat(binding.signUpButton, View.ALPHA, 1f).setDuration(100)

        val alreadyAndSignUpAnimation = AnimatorSet().apply {
            playTogether(
                alreadyHaveAcc,
                signInButton
            )
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                name,
                nameInputField,
                email,
                emailInputField,
                password,
                passwordInputField,
                signUpButton,
                alreadyAndSignUpAnimation
            )
            startDelay = 100
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}