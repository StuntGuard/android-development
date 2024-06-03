package id.project.stuntguard.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import id.project.stuntguard.databinding.FragmentHomeBinding
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.view.onboarding.OnboardingActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Checking -> is user already login?? :
        viewModel.getSession().observe(requireActivity()) { user ->
            if (!user.isLogin) {
                val intentToOnboarding = Intent(requireActivity(), OnboardingActivity::class.java)
                startActivity(intentToOnboarding)
            }
        }

        setupAction()

        // TODO
        binding.seeAll.setOnClickListener {
            Toast.makeText(requireActivity(), "See All clicked", Toast.LENGTH_SHORT).show()
        }

        // TODO

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAction() {
        binding.logout.setOnClickListener {
            viewModel.logout()
        }
    }
}