package id.project.stuntguard.view.analyze

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import id.project.stuntguard.databinding.FragmentAnalyzeBinding

class AnalyzeFragment : Fragment() {
    private var _binding: FragmentAnalyzeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authToken = arguments?.getString("analyzeToken").toString()

        setupAction(authToken)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAction(authToken: String) {
        binding.addChildButton.setOnClickListener {
            val intentToAddChild = Intent(requireActivity(), AddChildActivity::class.java)
            intentToAddChild.putExtra(AddChildActivity.EXTRA_TOKEN, authToken)
            startActivity(intentToAddChild)
        }
    }
}