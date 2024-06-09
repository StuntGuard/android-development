package id.project.stuntguard.view.mission

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.project.stuntguard.databinding.FragmentMissionBinding
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.view.analyze.AddChildActivity

class MissionFragment : Fragment() {
    private var _binding: FragmentMissionBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MissionViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var adapter: MissionAdapter

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
        val authToken = arguments?.getString("missionToken").toString()

//        Still don't know how to get this idChild
        viewModel.getMissions(authToken = authToken, idChild = 28)

        setupView(authToken = authToken, idChild = 28)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView(authToken: String, idChild: Int) {
        val adapter = MissionAdapter()
        binding.apply {
            viewModel.getMissionResponse.observe(viewLifecycleOwner) { response ->
                if (response.data.isEmpty()) {
                    showErrorMessage(true)
                } else {
                    showErrorMessage(false)
                    adapter.submitList(response.data)
                }
            }
            rvMission.layoutManager = LinearLayoutManager(requireActivity())
            rvMission.adapter = adapter

            addMissionButton.setOnClickListener {
                val intentToAddMission = Intent(requireActivity(), AddMissionActivity::class.java)
                intentToAddMission.putExtra(AddMissionActivity.EXTRA_TOKEN, authToken)
                intentToAddMission.putExtra(AddMissionActivity.EXTRA_ID, idChild)
                startActivity(intentToAddMission)
            }
        }

        adapter.setOnItemClickCallback(object : MissionAdapter.OnClickCallback {
            override fun onItemClicked(idMission: Int, missionTitle: String) {
//                No detail activity
            }

            override fun onDeleteClicked(idMission: Int) {
                viewModel.deleteMission(authToken = authToken, idMission = idMission)
            }
        })
    }

    private fun showErrorMessage(isError: Boolean) {
        binding.noDataMessage.visibility = if (isError) View.VISIBLE else View.GONE
        binding.rvMission.visibility = if (isError) View.GONE else View.VISIBLE
    }
}