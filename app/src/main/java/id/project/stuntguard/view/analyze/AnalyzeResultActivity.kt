package id.project.stuntguard.view.analyze

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.project.stuntguard.R
import id.project.stuntguard.databinding.ActivityAnalyzeResultBinding

class AnalyzeResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyzeResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO
    }
}