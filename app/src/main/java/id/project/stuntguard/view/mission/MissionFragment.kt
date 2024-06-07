package id.project.stuntguard.view.mission

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import id.project.stuntguard.databinding.FragmentMissionBinding
import id.project.stuntguard.utils.helper.ViewModelFactory

class MissionFragment : Fragment() {
    private var _binding: FragmentMissionBinding? = null
    private val binding get() = _binding!!

//    private lateinit var missionViewModel: MissionViewModel
    private val missionViewModel by viewModels<MissionViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var missionAdapter: MissionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        missionViewModel = ViewModelProvider(this).get(MissionViewModel::class.java)
        missionAdapter = MissionAdapter(emptyList())

        binding.rvMission.adapter = missionAdapter

        val authToken = arguments?.getString("homeToken")

        missionViewModel.getMissions(authToken, 8)

//        missionViewModel.missionList.observe(viewLifecycleOwner) { missions ->
//            missionAdapter.submitList(missions)
//        }

        binding.addMissionButton.setOnClickListener {
            val context = requireActivity()
            val addMissionActivity = Intent(context, AddMissionActivity::class.java)
            startActivity(addMissionActivity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}