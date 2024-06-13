package id.project.stuntguard.view.login

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
import id.project.stuntguard.databinding.ActivityLoginBinding
import id.project.stuntguard.utils.component.CustomAlertDialog
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.view.main.MainActivity
import id.project.stuntguard.view.reset.CheckEmailActivity
import id.project.stuntguard.view.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        binding.apply {
            signInButton.setOnClickListener {
                val email = binding.emailEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString().trim()

                viewModel.signIn(email = email, password = password)
            }

            signUpClickable.setOnClickListener {
                val intentToSignUp = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(intentToSignUp)
            }

            forgotPassword.setOnClickListener {
                val intentToCheckEmail = Intent(this@LoginActivity, CheckEmailActivity::class.java)
                startActivity(intentToCheckEmail)
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.signInResponse.observe(this) { response ->
            val customAlertDialog = CustomAlertDialog(this)
            customAlertDialog.create(
                title = "Welcome!",
                message = "Hi, ${response.name}"
            ) {
                val intentToMain = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intentToMain)
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
                    onPositiveButtonClick Function :
                    ~ Do Nothing ~
                */
            }
            customAlertDialog.show()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.loginImage, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.loginTitle, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailInputField =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordInputField =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signInButton =
            ObjectAnimator.ofFloat(binding.signInButton, View.ALPHA, 1f).setDuration(100)
        val alreadyHaveAcc =
            ObjectAnimator.ofFloat(binding.alreadyHaveAccount, View.ALPHA, 1f).setDuration(100)
        val signUpButton =
            ObjectAnimator.ofFloat(binding.signUpClickable, View.ALPHA, 1f).setDuration(100)

        val alreadyAndSignUpAnimation = AnimatorSet().apply {
            playTogether(
                alreadyHaveAcc,
                signUpButton
            )
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                email,
                emailInputField,
                password,
                passwordInputField,
                signInButton,
                alreadyAndSignUpAnimation
            )
            startDelay = 100
        }.start()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }
}