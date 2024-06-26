package id.project.stuntguard.view.analyze

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import id.project.stuntguard.R
import id.project.stuntguard.data.model.ChildModel
import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.databinding.FragmentAnalyzeBinding
import id.project.stuntguard.utils.component.CustomAlertDialog
import id.project.stuntguard.utils.helper.ViewModelFactory

class AnalyzeFragment : Fragment() {
    private var _binding: FragmentAnalyzeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AnalyzeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var customAlertDialog: CustomAlertDialog
    private var listChild = arrayListOf<ChildModel>()
    private var listChildName = arrayListOf<String>()
    private var idPredictLog = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authToken = arguments?.getString("analyzeToken").toString()
        customAlertDialog = CustomAlertDialog(requireActivity())

        viewModel.getAllChild(authToken = authToken)
        viewModel.getAllChildResponse.observe(viewLifecycleOwner) { response ->
            setupChildList(response)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        setupAction(authToken)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // to Refresh the Fragment after Successfully Adding new Child Data :
    override fun onResume() {
        super.onResume()
        val authToken = arguments?.getString("analyzeToken").toString()
        viewModel.getAllChild(authToken = authToken)
    }

    private fun setupAction(authToken: String) {
        binding.apply {
            addChildButton.setOnClickListener {
                val intentToAddChild = Intent(requireActivity(), AddChildActivity::class.java)
                intentToAddChild.putExtra(AddChildActivity.EXTRA_TOKEN, authToken)
                startActivity(intentToAddChild)
            }
            analyzeButton.setOnClickListener {
                analyze(authToken = authToken)
            }
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

    private fun analyze(authToken: String) {
        val childName = binding.childNameDropdown.text.toString().trim()
        val age = binding.childAgeEditText.text.toString().trim().toIntOrNull() ?: 0
        val height = binding.childHeightEditText.text.toString().trim().toDoubleOrNull() ?: 0.0
        var childData: ChildModel? = null

        for (child in listChild) {
            if (childName == child.name) {
                childData = child
            }
        }

        if (childData != null && age > 0 && height > 0.0) {
            viewModel.predict(
                authToken = authToken,
                childData = childData,
                age = age,
                height = height
            )

            // to get PredictChild Response :
            viewModel.predictChildResponse.observe(viewLifecycleOwner) { response ->
                /*
                    Using idPredictLog as a helper to ensure idPredict from response isn't
                    duplicated and don't run setupPredictResult more than once with the same idPredict
                */
                if (idPredictLog == 0 || idPredictLog != response.data.id) {
                    idPredictLog = response.data.id
                    setupPredictResult(authToken = authToken, idPredict = response.data.id)
                }
            }

        } else {
            customAlertDialog.apply {
                create(
                    title = "Invalid",
                    message = "Make Sure the Input Correct!!\n\n\t[ Please choose a child, Age > 0 \n\tand Height > 0 ]"
                ) {
                    // Do Nothing
                }
                show()
            }
        }
    }

    private fun setupPredictResult(authToken: String, idPredict: Int) {
        // To move to AnalyzeResultActivity :
        val intentToAnalyzeResult = Intent(requireActivity(), AnalyzeResultActivity::class.java)
        intentToAnalyzeResult.putExtra(AnalyzeResultActivity.EXTRA_TOKEN, authToken)
        intentToAnalyzeResult.putExtra(AnalyzeResultActivity.EXTRA_ID_PREDICT, idPredict)
        startActivity(intentToAnalyzeResult)

        // To reset input field :
        binding.apply {
            childAgeEditText.text = null
            childHeightEditText.text = null
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}