package id.project.stuntguard.view.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.project.stuntguard.R
import id.project.stuntguard.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO
    }
}