package id.project.stuntguard.utils.adapters.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.project.stuntguard.data.remote.response.DataHistory
import id.project.stuntguard.databinding.ItemHistoryDetailBinding
import id.project.stuntguard.utils.helper.formatDate
import id.project.stuntguard.utils.helper.setResultTextColor

class ChildDetailHistoryListAdapter :
    ListAdapter<DataHistory, ChildDetailHistoryListAdapter.ListViewHolder>(
        DIFF_CALLBACK
    ) {
    private lateinit var onClickCallback: OnClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemHistoryDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataChildHistory = getItem(position)
        holder.apply {
            bind(dataChildHistory)
            itemView.setOnClickListener {
                onClickCallback.onItemClicked(dataChildHistory.id)
            }
        }
    }

    class ListViewHolder(val binding: ItemHistoryDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataChildHistory: DataHistory) {
            binding.apply {
                profileName.text = dataChildHistory.name
                status.text = dataChildHistory.prediction

                status.setTextColor(
                    setResultTextColor(
                        context = root.context,
                        resultPrediction = dataChildHistory.prediction
                    )[0]
                )

                val formattedDate = formatDate(dataChildHistory.createdAt)
                date.text = formattedDate
            }
        }
    }

    fun setOnItemClickCallback(onClickCallback: OnClickCallback) {
        this.onClickCallback = onClickCallback
    }

    interface OnClickCallback {
        fun onItemClicked(idPredict: Int)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataHistory>() {
            override fun areItemsTheSame(
                oldItem: DataHistory,
                newItem: DataHistory
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataHistory,
                newItem: DataHistory
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}