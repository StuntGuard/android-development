package id.project.stuntguard.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.project.stuntguard.R
import id.project.stuntguard.data.model.ChildModel
import id.project.stuntguard.data.remote.response.DataMission
import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.databinding.FragmentHomeBinding
import id.project.stuntguard.utils.adapters.mission.MissionListAdapter
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.utils.helper.formatDate
import id.project.stuntguard.utils.helper.setResultTextColor
import id.project.stuntguard.view.analyze.AddChildActivity
import id.project.stuntguard.view.analyze.AnalyzeResultActivity
import id.project.stuntguard.view.mission.AddMissionActivity
import id.project.stuntguard.view.mission.MissionFragment

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var listChild = arrayListOf<ChildModel>()
    private var listChildName = arrayListOf<String>()

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

        val authToken = arguments?.getString("homeToken").toString()
        val userName = arguments?.getString("userName").toString()
        binding.user.text = userName

        viewModel.getAllChild(authToken = authToken)

        setupView(authToken = authToken)
        setupAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // To refresh HomeFragment when ButtonCard get clicked and navigated to other screen then back to here :
    override fun onResume() {
        super.onResume()
        val authToken = arguments?.getString("homeToken").toString()
        viewModel.getAllChild(authToken = authToken)
    }

    private fun setupView(authToken: String) {
        viewModel.getAllChildResponse.observe(viewLifecycleOwner) { response ->
            setupChildList(response)
            setupCardView(authToken)
            setupMissionList(authToken)
        }

        // Listener for the dropdown selection to keep cardView update based on selection :
        binding.childNameDropdown.setOnItemClickListener { _, _, _, _ ->
            setupCardView(authToken = authToken)
            setupMissionList(authToken)
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

    private fun setupCardView(authToken: String) {
        binding.apply {
            val childName = childNameDropdown.text.toString().trim()
            var childData: ChildModel? = null

            for (child in listChild) {
                if (childName == child.name) {
                    childData = child
                }
            }

            if (childData != null) {
                viewModel.getChildPredictHistory(authToken = authToken, idChild = childData.id)
                viewModel.getChildPredictHistoryResponse.observe(viewLifecycleOwner) { response ->

                    if (response.data.isEmpty()) {
                        isCardDataProvided(false)
                        noDataMessageCard.text = "Try to Analyze Your Child"
                        buttonCard.setOnClickListener {
                            navigateToOtherFragment(R.id.navigation_analyze)
                        }

                    } else {
                        Glide.with(profileImage.context)
                            .load(childData.urlPhoto)
                            .circleCrop()
                            .into(profileImage)
                        profileName.text = response.data[0].name
                        conditionStatus.text = response.data[0].subtitle
                        conditionResult.text = response.data[0].prediction

                        // Set color of conditionResult :
                        conditionResult.setTextColor(
                            setResultTextColor(
                                context = requireActivity(),
                                response.data[0].prediction
                            )[0]
                        )

                        // Set color of conditionStatus :
                        conditionStatus.setTextColor(
                            setResultTextColor(
                                context = requireActivity(),
                                response.data[0].subtitle
                            )[1]
                        )

                        val formattedDate = formatDate(response.data[0].createdAt)
                        profileDate.text = formattedDate

                        isCardDataProvided(true)

                        // Action when CardView get clicked :
                        childCardView.setOnClickListener {
                            val intentToAnalyzeResult = Intent(requireActivity(), AnalyzeResultActivity::class.java)
                            intentToAnalyzeResult.putExtra(AnalyzeResultActivity.EXTRA_TOKEN, authToken)
                            intentToAnalyzeResult.putExtra(AnalyzeResultActivity.EXTRA_ID_PREDICT, response.data[0].id)
                            startActivity(intentToAnalyzeResult)
                        }
                    }
                }

            } else {
                isCardDataProvided(false)
                noDataMessageCard.text = "Please Add Your New Child Data"
                buttonCard.setOnClickListener {
                    val intentToAddNewChild =
                        Intent(requireActivity(), AddChildActivity::class.java)
                    intentToAddNewChild.putExtra(AddChildActivity.EXTRA_TOKEN, authToken)
                    startActivity(intentToAddNewChild)
                }
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
                        showMissionData(false)

                    } else {
                        showMissionData(true)

                        // Take only 3 Mission Data to displayed in the rvMission / list :
                        adapter.submitList(response.data.take(3))
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
                showMissionData(false)
            }
        }
    }

    private fun setupAction() {
        binding.logout.setOnClickListener {
            viewModel.logout()
        }
        binding.seeAll.setOnClickListener {
            navigateToOtherFragment(R.id.navigation_mission)
        }
    }

    private fun navigateToOtherFragment(destinationFragmentId: Int) {
        val navController = findNavController()
        val bottomNavView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

        navController.navigate(destinationFragmentId)
        bottomNavView.selectedItemId = destinationFragmentId
    }

    private fun isCardDataProvided(hasData: Boolean) {
        binding.apply {
            // Default Component :
            profileImage.visibility = if (hasData) View.VISIBLE else View.GONE
            profileName.visibility = if (hasData) View.VISIBLE else View.GONE
            conditionStatus.visibility = if (hasData) View.VISIBLE else View.GONE
            conditionResult.visibility = if (hasData) View.VISIBLE else View.GONE
            profileMessage.visibility = if (hasData) View.VISIBLE else View.GONE
            profileDate.visibility = if (hasData) View.VISIBLE else View.GONE

            // Error Component :
            noDataCard.visibility = if (hasData) View.GONE else View.VISIBLE
            noDataMessageCard.visibility = if (hasData) View.GONE else View.VISIBLE
            buttonCard.visibility = if (hasData) View.GONE else View.VISIBLE
        }
    }

    private fun showMissionData(hasData: Boolean) {
        binding.apply {
            rvMission.visibility = if (hasData) View.VISIBLE else View.GONE

            noDataMessage.visibility = if (hasData) View.GONE else View.VISIBLE
        }
    }
}