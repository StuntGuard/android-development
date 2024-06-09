package id.project.stuntguard.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import id.project.stuntguard.R
import id.project.stuntguard.data.model.ChildModel
import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.databinding.FragmentHomeBinding
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.view.mission.AddMissionActivity
import id.project.stuntguard.view.mission.HomeAdapter
import id.project.stuntguard.view.mission.MissionAdapter
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var listChild = arrayListOf<ChildModel>()
    private var listChildName = arrayListOf<String>()
    private var selectedChildId: Int? = null
    private var idPredictLog = 0

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

        val authToken = arguments?.getString("missionToken").toString()

        viewModel.getMissions(authToken = authToken, idChild = 27)

        setupView(authToken = authToken, idChild = 27)

//        Latest Child
        viewModel.getAllChild(authToken = authToken)
        viewModel.getAllChildResponse.observe(viewLifecycleOwner) { response ->
            setupChildList(response)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun setupView(authToken: String, idChild: Int) {
        val adapter = HomeAdapter()
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
            onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                selectedChildId = listChild[position].id
                updateChildCard(listChild[position])
            }
        }
    }

    private fun updateChildCard(child: ChildModel) {
        binding.childNameTextView.text = child.name
        binding.childGenderTextView.text = child.gender
        // Set age if available
        // binding.childAgeTextView.text = ...

        Glide.with(this)
            .load(child.urlPhoto)
            .placeholder(R.drawable.ic_person)
            .into(binding.childImageView)
    }

    private fun showErrorMessage(isError: Boolean) {
        binding.noDataMessage.visibility = if (isError) View.VISIBLE else View.GONE
        binding.rvMission.visibility = if (isError) View.GONE else View.VISIBLE
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}