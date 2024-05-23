package id.project.stuntguard.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import id.project.stuntguard.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.logout.setOnClickListener {
            Toast.makeText(requireActivity(), "Logout clicked", Toast.LENGTH_SHORT).show()
        }

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
}