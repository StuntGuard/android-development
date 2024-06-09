package id.project.stuntguard.view.mission

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.project.stuntguard.R
import id.project.stuntguard.data.model.ChildModel
import id.project.stuntguard.data.remote.response.GetAllChildResponse
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
    private var listChild = arrayListOf<ChildModel>()
    private var listChildName = arrayListOf<String>()
    private var selectedChildId: Int? = null

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
        var idChild = 27
        val childName = binding.childNameDropdown.text.toString().trim()

        var childData: ChildModel? = null

        for (child in listChild) {
            if (childName == child.name) {
                childData = child
            }
        }

        if (childData != null) {
            idChild = childData.id
        }

        viewModel.getMissions(authToken = authToken, idChild = idChild)

        setupView(authToken = authToken, idChild = idChild)

        viewModel.getAllChild(authToken = authToken)
        viewModel.getAllChildResponse.observe(viewLifecycleOwner) { response ->
            setupChildList(response)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

//        setupObservers(authToken)
//        setupDropdown(authToken)

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
//                intentToAddMission.putExtra(AddMissionActivity.EXTRA_ID, idChild)
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

    private fun setupObservers(authToken: String) {
        viewModel.getMissionResponse.observe(viewLifecycleOwner) { response ->
            if (response.data.isEmpty()) {
                showErrorMessage(true)
            } else {
                showErrorMessage(false)
                adapter.submitList(response.data)
            }
        }

        viewModel.getAllChildResponse.observe(viewLifecycleOwner) { response ->
            setupChildList(response)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.getAllChild(authToken)
    }

    private fun setupDropdown(authToken: String) {
        binding.childNameDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                selectedChildId = listChild[position].id
                selectedChildId?.let { viewModel.getMissions(authToken = authToken, idChild = it) }
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
        }
    }

    private fun showErrorMessage(isError: Boolean) {
        binding.noDataMessage.visibility = if (isError) View.VISIBLE else View.GONE
        binding.rvMission.visibility = if (isError) View.GONE else View.VISIBLE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}