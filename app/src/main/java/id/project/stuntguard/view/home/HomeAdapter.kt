package id.project.stuntguard.view.mission

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.project.stuntguard.data.remote.response.DataMission
import id.project.stuntguard.databinding.ItemMissionBinding

class HomeAdapter : ListAdapter<DataMission, HomeAdapter.HomeViewHolder>(
    HomeAdapter.DIFF_CALLBACK
) {
    private lateinit var onClickCallback: OnClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemMissionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val dataMission = getItem(position)
        holder.apply {
            bind(dataMission)
            itemView.setOnClickListener {
                onClickCallback.onItemClicked(dataMission.id, dataMission.title)
            }
            binding.delete.isVisible = false
        }
    }

    class HomeViewHolder(val binding: ItemMissionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataMission: DataMission) {
            binding.apply {
                missionTitle.text = dataMission.title
                missionDescription.text = dataMission.description
            }
        }
    }

    fun setOnItemClickCallback(onClickCallback: HomeAdapter.OnClickCallback) {
        this.onClickCallback = onClickCallback
    }

    interface OnClickCallback {
        fun onItemClicked(idMission: Int, missionTitle: String)
        fun onDeleteClicked(idMission: Int)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataMission>() {
            override fun areItemsTheSame(
                oldItem: DataMission,
                newItem: DataMission
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataMission,
                newItem: DataMission
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
