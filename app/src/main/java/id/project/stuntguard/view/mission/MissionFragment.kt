package id.project.stuntguard.view.mission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import id.project.stuntguard.databinding.FragmentMissionBinding

class MissionFragment : Fragment() {
    private var _binding: FragmentMissionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMissionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addMissionButton.setOnClickListener {
            Toast.makeText(requireActivity(), "Add Mission Clicked", Toast.LENGTH_SHORT).show()
        }

        // TODO

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}