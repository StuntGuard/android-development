package id.project.stuntguard.view.analyze

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.project.stuntguard.R
import id.project.stuntguard.data.remote.response.Data
import id.project.stuntguard.databinding.ActivityAnalyzeResultBinding
import id.project.stuntguard.utils.adapters.analyze.RecommendationsListAdapter
import id.project.stuntguard.utils.helper.ViewModelFactory
import id.project.stuntguard.utils.helper.formatDigit
import id.project.stuntguard.utils.helper.setResultTextColor

class AnalyzeResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyzeResultBinding
    private val viewModel by viewModels<AnalyzeViewModel> {
        ViewModelFactory.getInstance(this@AnalyzeResultActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authToken = intent.getStringExtra(EXTRA_TOKEN).toString()
        val idPredict = intent.getIntExtra(EXTRA_ID_PREDICT, 0)

        // to get Predict Result using idPredict :
        viewModel.getPredictResult(authToken = authToken, idPredict = idPredict)

        viewModel.isLoading.observe(this@AnalyzeResultActivity) {
            showLoading(it)
        }

        setupView()
        setupAction()
    }

    private fun setupView() {
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

        viewModel.getPredictResultResponse.observe(this@AnalyzeResultActivity) { response ->
            setupPredictResultView(response.data)
        }
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            // to Remove AnalyzeResultActivity and back to AnalyzeFragment :
            finish()
        }
    }

    private fun setupPredictResultView(data: Data) {
        binding.apply {
            if (data.prediction == "Stunted" || data.prediction == "Severely Stunted") {
                resultImage.setImageResource(R.drawable.negative_analyze_result)
            } else {
                resultImage.setImageResource(R.drawable.positive_analyze_result)
            }

            // to Format percentage data :
            val percentage = "${data.score.toString().toDouble().formatDigit(2)} %"

            // setup View with data :
            analyzePercentage.text = percentage
            analyzePrediction.text = data.prediction
            analyzeSubPrediction.text = data.subtitle
            analyzeMessage.text = data.message

            analyzePrediction.setTextColor(
                setResultTextColor(
                    context = this@AnalyzeResultActivity,
                    data.prediction
                )[0]
            )

            analyzeSubPrediction.setTextColor(
                setResultTextColor(
                    context = this@AnalyzeResultActivity,
                    data.prediction
                )[1]
            )

            // set the RecyclerView for list of Recommendations :
            val adapter = RecommendationsListAdapter()
            adapter.submitList(data.recommendations)
            rvAnalyzeRecommendation.layoutManager = LinearLayoutManager(this@AnalyzeResultActivity)
            rvAnalyzeRecommendation.adapter = adapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_ID_PREDICT = "extra_id_predict"
    }
}