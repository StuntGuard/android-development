package id.project.stuntguard.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.project.stuntguard.databinding.ActivitySignupBinding
import id.project.stuntguard.utils.component.CustomAlertDialog
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

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
    }

    private fun setupAction() {
        binding.signUpButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            viewModel.signUp(name = name, email = email, password = password)
        }

        binding.signInClickable.setOnClickListener {
            val intentToSignIn = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intentToSignIn)
            finish()
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.signUpResponse.observe(this) { response ->
            val customAlertDialog = CustomAlertDialog(this)
            customAlertDialog.create(
                title = "Success",
                message = response.message
            ) {
                val intentToSignIn = Intent(this@SignupActivity, LoginActivity::class.java)
                startActivity(intentToSignIn)
                finish()
            }
            customAlertDialog.show()
        }

        viewModel.errorResponse.observe(this) { errorMessage ->
            val customAlertDialog = CustomAlertDialog(this)
            customAlertDialog.create(
                title = "Invalid",
                message = errorMessage.toString()
            ) {
                /*
                    onPositiveButtonClick :
                    ~ Do Nothing ~
                */
            }
            customAlertDialog.show()
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