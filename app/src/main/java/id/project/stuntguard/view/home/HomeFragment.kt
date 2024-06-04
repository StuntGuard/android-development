package id.project.stuntguard.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import id.project.stuntguard.databinding.FragmentHomeBinding
import id.project.stuntguard.utils.helper.ViewModelFactory

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()

        /*
            Note ~ Fidel :
            How to use the Authorization Token?

            -> Uncomment this section to try it out :

            // val authToken = arguments?.getString("homeToken")
            // binding.seeAll.text = authToken

            -> how to know the key of arguments? :
            -> It provided on navigation_graph.xml, argument's key name is located on fragment > argument > name

            -> How I know it work? :
            -> The textView on "See All" that should be show text "See All" will change to "Bearer Ey5dfc45..." on right center of the screen
        */

        // TODO :

        binding.seeAll.setOnClickListener {
            Toast.makeText(requireActivity(), "See All clicked", Toast.LENGTH_SHORT).show()
        }
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