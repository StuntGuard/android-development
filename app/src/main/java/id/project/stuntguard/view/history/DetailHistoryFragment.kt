package id.project.stuntguard.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.project.stuntguard.databinding.FragmentDetailHistoryBinding

class DetailHistoryFragment : Fragment() {
    private var _binding: FragmentDetailHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TODO

        return root
    }
}