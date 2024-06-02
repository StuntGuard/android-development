package id.project.stuntguard.view.analyze

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.project.stuntguard.R
import id.project.stuntguard.databinding.ActivityAddChildBinding

class AddChildActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddChildBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChildBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO
    }
}