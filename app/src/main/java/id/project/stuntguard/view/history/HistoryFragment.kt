package id.project.stuntguard.view.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.project.stuntguard.databinding.FragmentHistoryBinding
import id.project.stuntguard.utils.adapters.history.ChildListAdapter
import id.project.stuntguard.utils.helper.ViewModelFactory

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authToken = arguments?.getString("historyToken").toString()

        viewModel.getAllChild(authToken = authToken)
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        setupView(authToken = authToken)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView(authToken: String) {
        val adapter = ChildListAdapter()
        binding.apply {
            viewModel.getAllChildResponse.observe(viewLifecycleOwner) { response ->
                if (response.data.isEmpty()) {
                    showErrorMessage(true)
                } else {
                    showErrorMessage(false)
                    adapter.submitList(response.data)
                }
            }
            rvHistory.layoutManager = LinearLayoutManager(requireActivity())
            rvHistory.adapter = adapter
        }

        // to Set an action when child item on RecyclerView List get clicked :
        adapter.setOnItemClickCallback(object : ChildListAdapter.OnClickCallback {
            override fun onItemClicked(idChild: Int, childName: String) {
                // Move to DetailHistoryListActivity and pass the idChild :
                val intentToDetailHistoryList = Intent(requireActivity(), DetailHistoryListActivity::class.java)
                intentToDetailHistoryList.putExtra(DetailHistoryListActivity.EXTRA_TOKEN, authToken)
                intentToDetailHistoryList.putExtra(DetailHistoryListActivity.EXTRA_ID_CHILD, idChild)
                intentToDetailHistoryList.putExtra(DetailHistoryListActivity.EXTRA_CHILD_NAME, childName)
                startActivity(intentToDetailHistoryList)
            }

            override fun onDeleteClicked(message: String) {
                // Action when Delete Icon get clicked :
                Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(isError: Boolean) {
        binding.noDataMessage.visibility = if (isError) View.VISIBLE else View.GONE
        binding.rvHistory.visibility = if (isError) View.GONE else View.VISIBLE
    }
}