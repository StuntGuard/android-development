package id.project.stuntguard.view.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import id.project.stuntguard.R
import id.project.stuntguard.data.remote.api.ApiConfig
import id.project.stuntguard.data.remote.response.SigninResponse
import id.project.stuntguard.data.remote.response.SignupResponse
import id.project.stuntguard.databinding.ActivitySignupBinding
import id.project.stuntguard.utils.component.EmailInputField
import id.project.stuntguard.utils.component.PasswordInputField
import id.project.stuntguard.view.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private lateinit var nameEditText: EmailInputField
    private lateinit var emailEditText: EmailInputField
    private lateinit var passwordEditText: PasswordInputField
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameEditText = binding.nameEditText
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText
        registerButton = binding.signUpButton

        setMyButtonEnable()
        showLoading(false)

        registerButton.setOnClickListener {
            register(
                nameEditText.text.toString(),
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
            showLoading(true)
        }

        binding.signInClickable.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun register(name: String, email: String, password: String) {
        val client = ApiConfig.getApiService().signup(name, email, password)
        client.enqueue(object: Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val signUpResponse = response.body()
                    if (signUpResponse != null) {
                        if (signUpResponse.status == "fail") {
                            Toast.makeText(
                                this@SignupActivity,
                                signUpResponse.message,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            val intent = Intent(this@SignupActivity, SigninResponse::class.java)
                                showLoading(false)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Toast.makeText(
                    this@SignupActivity,
                    "onFailure: ${t.message.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }

    private fun setMyButtonEnable() {
        val email = emailEditText.text
        val password = passwordEditText.text
        val name = nameEditText.text
        registerButton.isEnabled = (email != null && email.toString()
            .isNotEmpty()) && (password != null && password.toString()
            .isNotEmpty()) && (name != null && name.toString().isNotEmpty())
    }

    private fun showLoading(state: Boolean) {
//        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }

    private fun validateName(): Boolean{
        var error: String? = null
        val value: String = binding.nameEditText.text.toString()
        if (value.isEmpty()){
            error = "Name is required"
        }
        return error == null
    }

    private fun validateEmail(): Boolean{
        var error: String? = null
        val value = binding.emailEditText.text.toString()
        if (value.isEmpty()){
            error = "Email is required"
        } else if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            error = "Email address is invalid"
        }
        return error == null
    }

    private fun validatePassword(): Boolean {
        var error: String? = null
        val value = binding.passwordEditText.text.toString()
        if (value.isEmpty()){
            error = "Password is required"
        } else if (value.length < 8) {
            error = "Password must be 8 characters long"
        }
        return error == null
    }
}