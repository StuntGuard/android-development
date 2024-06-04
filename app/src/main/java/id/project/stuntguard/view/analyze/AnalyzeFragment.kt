package id.project.stuntguard.view.analyze

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
        val authToken = arguments?.getString("analyzeToken")

        // TODO

        binding.addChildButton.setOnClickListener {
            Toast.makeText(requireActivity(), "Add Child Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}