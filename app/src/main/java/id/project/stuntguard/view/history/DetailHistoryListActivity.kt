package id.project.stuntguard.view.history

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.project.stuntguard.databinding.ActivityDetailHistoryListBinding
import id.project.stuntguard.utils.adapters.history.ChildDetailHistoryListAdapter
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.view.analyze.AnalyzeResultActivity

class DetailHistoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHistoryListBinding
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this@DetailHistoryListActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authToken = intent.getStringExtra(EXTRA_TOKEN).toString()
        val idChild = intent.getIntExtra(EXTRA_ID_CHILD, 0)
        val childName = intent.getStringExtra(EXTRA_CHILD_NAME).toString()

        viewModel.getChildPredictHistory(authToken = authToken, idChild = idChild)
        viewModel.isLoading.observe(this@DetailHistoryListActivity) {
            showLoading(it)
        }

        setupView(authToken = authToken, childName = childName)
        setupAction()
    }

    private fun setupView(authToken: String, childName: String) {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        val adapter = ChildDetailHistoryListAdapter()
        binding.apply {
            val name = "$childName's History"
            title.text = name

            viewModel.getChildPredictHistoryResponse.observe(this@DetailHistoryListActivity) { response ->
                if (response.data.isEmpty()) {
                    showErrorMessage(true)

                } else {
                    showErrorMessage(false)
                    adapter.submitList(response.data)
                }
            }
            rvDetailHistory.layoutManager = LinearLayoutManager(this@DetailHistoryListActivity)
            rvDetailHistory.adapter = adapter
        }

        adapter.setOnItemClickCallback(object : ChildDetailHistoryListAdapter.OnClickCallback {
            override fun onItemClicked(idPredict: Int) {
                val intentToAnalyzeResult =
                    Intent(this@DetailHistoryListActivity, AnalyzeResultActivity::class.java)
                intentToAnalyzeResult.putExtra(AnalyzeResultActivity.EXTRA_TOKEN, authToken)
                intentToAnalyzeResult.putExtra(AnalyzeResultActivity.EXTRA_ID_PREDICT, idPredict)
                startActivity(intentToAnalyzeResult)
            }
        })
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(isError: Boolean) {
        binding.noDataMessage.visibility = if (isError) View.VISIBLE else View.GONE
        binding.rvDetailHistory.visibility = if (isError) View.GONE else View.VISIBLE
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_ID_CHILD = "extra_id_child"
        const val EXTRA_CHILD_NAME = "extra_child_name"
    }
}