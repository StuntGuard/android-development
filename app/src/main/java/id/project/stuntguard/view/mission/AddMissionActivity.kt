package id.project.stuntguard.view.mission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.project.stuntguard.R
import id.project.stuntguard.databinding.ActivityAddMissionBinding

class AddMissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO
    }
}