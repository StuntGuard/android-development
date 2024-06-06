package id.project.stuntguard.utils.adapters.analyze

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.project.stuntguard.data.remote.response.RecommendationsItem
import id.project.stuntguard.databinding.ItemAnalyzeRecommendationBinding

class RecommendationsListAdapter :
    ListAdapter<RecommendationsItem, RecommendationsListAdapter.ListViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemAnalyzeRecommendationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val recommendationData = getItem(position)
        holder.bind(recommendationData, position)
    }

    class ListViewHolder(val binding: ItemAnalyzeRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendationData: RecommendationsItem, index: Int) {
            binding.apply {
                val number = "${index + 1}."
                recommendationIndex.text = number
                recommendationTitle.text = recommendationData.title
                recommendationDescription.text = recommendationData.description
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendationsItem>() {
            override fun areItemsTheSame(
                oldItem: RecommendationsItem,
                newItem: RecommendationsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RecommendationsItem,
                newItem: RecommendationsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}