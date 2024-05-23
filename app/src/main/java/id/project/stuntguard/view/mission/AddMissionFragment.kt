package id.project.stuntguard.view.mission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.project.stuntguard.databinding.FragmentAddMissionBinding

class AddMissionFragment : Fragment() {
    private var _binding: FragmentAddMissionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMissionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TODO

        return root
    }
}