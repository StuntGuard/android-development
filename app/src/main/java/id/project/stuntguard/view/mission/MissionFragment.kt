package id.project.stuntguard.view.mission

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.project.stuntguard.R
import id.project.stuntguard.data.model.ChildModel
import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.databinding.FragmentMissionBinding
import id.project.stuntguard.utils.adapters.mission.MissionListAdapter
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.view.analyze.AddChildActivity

class MissionFragment : Fragment() {
    private var _binding: FragmentMissionBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MissionViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var listChild = arrayListOf<ChildModel>()
    private var listChildName = arrayListOf<String>()

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
        viewModel.getAllChild(authToken = authToken)

        setupView(authToken)
        setupAction(authToken = authToken)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // to Refresh MissionFragment when noDataButton get clicked and navigated to other screen then back to here :
    override fun onResume() {
        super.onResume()
        val authToken = arguments?.getString("missionToken").toString()
        viewModel.getAllChild(authToken = authToken)
    }

    private fun setupView(authToken: String) {
        viewModel.getAllChildResponse.observe(viewLifecycleOwner) { response ->
            setupChildList(response)
            setupMissionList(authToken = authToken)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setupAction(authToken: String) {
        binding.addMissionButton.setOnClickListener {
            val intentToAddMission = Intent(requireActivity(), AddMissionActivity::class.java)
            intentToAddMission.putExtra(AddMissionActivity.EXTRA_TOKEN, authToken)
            startActivity(intentToAddMission)
        }

        // Listener for the dropdown selection to keep Mission List update based on selection :
        binding.childNameDropdown.setOnItemClickListener { _, _, _, _ ->
            setupMissionList(authToken = authToken)
        }
    }

    private fun setupChildList(response: GetAllChildResponse) {
        listChild.clear()
        listChildName.clear()

        for (child in response.data) {
            val childData = ChildModel(
                id = child.id,
                name = child.name,
                urlPhoto = child.urlPhoto,
                gender = child.gender
            )
            listChild.add(childData)
            listChildName.add(child.name)
        }

        val childNameOptions = listChildName
        val childOptionsAdapter =
            ArrayAdapter(requireActivity(), R.layout.dropdown_item, childNameOptions)
        binding.childNameDropdown.apply {
            setAdapter(childOptionsAdapter)
            setDropDownBackgroundResource(R.color.medium_grey)

            // To set default selected child on Dropdown Input :
            if (listChildName.isNotEmpty()) {
                setText(listChildName[0], false)
            } else {
                setText(R.string.choose_your_child)
            }
        }
    }

    private fun setupMissionList(authToken: String) {
        binding.apply {
            val adapter = MissionListAdapter()
            val childName = childNameDropdown.text.toString().trim()
            var childData: ChildModel? = null

            for (child in listChild) {
                if (child.name == childName) {
                    childData = child
                }
            }

            if (childData != null) {
                viewModel.getMissions(authToken = authToken, idChild = childData.id)
                viewModel.getMissionResponse.observe(viewLifecycleOwner) { response ->
                    if (response.data.isEmpty()) {
                        showData(false)
                        noDataSubMessage.text = getString(R.string.try_to_add_new_mission)
                        noDataButton.setOnClickListener {
                            val intentToAddMission =
                                Intent(requireActivity(), AddMissionActivity::class.java)
                            intentToAddMission.putExtra(AddMissionActivity.EXTRA_TOKEN, authToken)
                            startActivity(intentToAddMission)
                        }

                    } else {
                        showData(true)
                        adapter.submitList(response.data)
                    }
                }
                rvMission.adapter = adapter
                rvMission.layoutManager = LinearLayoutManager(requireActivity())

                adapter.setOnItemClickCallback(object : MissionListAdapter.OnClickCallback {
                    override fun onDeleteClicked(idMission: Int) {
                        viewModel.deleteMission(authToken = authToken, idMission = idMission)
                    }
                })

            } else {
                showData(false)
                noDataSubMessage.text = getString(R.string.please_add_your_new_child_data)
                noDataButton.setOnClickListener {
                    val intentToAddChild = Intent(requireActivity(), AddChildActivity::class.java)
                    intentToAddChild.putExtra(AddChildActivity.EXTRA_TOKEN, authToken)
                    startActivity(intentToAddChild)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showData(hasData: Boolean) {
        binding.apply {
            rvMission.visibility = if (hasData) View.VISIBLE else View.GONE

            noDataMessage.visibility = if (hasData) View.GONE else View.VISIBLE
            noDataSubMessage.visibility = if (hasData) View.GONE else View.VISIBLE
            noDataButton.visibility = if (hasData) View.GONE else View.VISIBLE
        }
    }
}