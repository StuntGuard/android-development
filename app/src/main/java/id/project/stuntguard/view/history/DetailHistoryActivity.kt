package id.project.stuntguard.view.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.project.stuntguard.R
import id.project.stuntguard.databinding.ActivityDetailHistoryBinding

class DetailHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO
    }
}