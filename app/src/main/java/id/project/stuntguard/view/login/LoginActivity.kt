package id.project.stuntguard.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import id.project.stuntguard.R
import id.project.stuntguard.data.preferences.UserModel
import id.project.stuntguard.data.preferences.UserPreferences
import id.project.stuntguard.data.remote.api.ApiConfig
import id.project.stuntguard.data.remote.response.SigninResponse
import id.project.stuntguard.databinding.ActivityLoginBinding
import id.project.stuntguard.utils.component.EmailInputField
import id.project.stuntguard.utils.component.PasswordInputField
import id.project.stuntguard.view.main.MainActivity
import id.project.stuntguard.view.signup.SignupActivity
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import java.util.prefs.Preferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var emailEditText: EmailInputField
    private lateinit var passwordEditText: PasswordInputField
    private lateinit var loginButton: Button

    private lateinit var myPreference: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText
        loginButton = binding.signInButton

        if (myPreference.getSession().first().isLogin){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

//        showLoading(false)
        setMyButtonEnable()

        binding.signUpClickable.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginButton.setOnClickListener {
            login(emailEditText.text.toString(), passwordEditText.text.toString())
            showLoading(true)
        }


    }

    private fun setMyButtonEnable() {
        val email = emailEditText.text
        val password = passwordEditText.text
        loginButton.isEnabled = (email != null && email.toString()
            .isNotEmpty()) && (password != null && password.toString().isNotEmpty())
    }

    private fun login(email: String, password: String) {
        val client = ApiConfig.getApiService().signin(email, password)
        client.enqueue(object : Callback<SigninResponse> {
            override fun onResponse(call: Call<SigninResponse>, response: Response<SigninResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        if (loginResponse.status == "fail") {
                            Toast.makeText(this@LoginActivity, "Login Failed!", Toast.LENGTH_LONG)
                                .show()
                        } else {
//                            myPreference.saveSession(UserModel(email, password, true))
//                            myPreference.setStatusLogin(true)
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            showLoading(false)
                            startActivity(intent)
                            finishAffinity()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "onFailure: ${t.message.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun showLoading(state: Boolean) {
//        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }


}