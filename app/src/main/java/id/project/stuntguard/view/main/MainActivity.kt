package id.project.stuntguard.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import id.project.stuntguard.R
import id.project.stuntguard.databinding.ActivityMainBinding
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.view.onboarding.OnboardingActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)

        // Checking -> is user already login?? :
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intentToOnboarding = Intent(this@MainActivity, OnboardingActivity::class.java)
                startActivity(intentToOnboarding)
            } else {
                // set NavGraph :
                val navGraph = navController.navInflater.inflate(R.navigation.navigation_graph)

                // Set Authorization Token as Arguments/bundle(Safe args) to send it to all other Fragment :
                val bundle = Bundle().apply {
                    putString("homeToken", "Bearer ${user.token}")
                    putString("analyzeToken", "Bearer ${user.token}")
                    putString("missionToken", "Bearer ${user.token}")
                    putString("historyToken", "Bearer ${user.token}")
                }

                // Set NavGraph to NavController and set startingDestinations Arguments :
                navController.setGraph(navGraph, bundle)

                // Set Other Destinations Arguments :
                binding.navView.setOnItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.navigation_home -> {
                            navController.navigate(R.id.navigation_home, bundle)
                            true
                        }

                        R.id.navigation_analyze -> {
                            navController.navigate(R.id.navigation_analyze, bundle)
                            true
                        }

                        R.id.navigation_mission -> {
                            navController.navigate(R.id.navigation_mission, bundle)
                            true
                        }

                        R.id.navigation_history -> {
                            navController.navigate(R.id.navigation_history, bundle)
                            true
                        }

                        else -> false
                    }
                }
            }
        }

        setupView()
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}