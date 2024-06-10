package id.project.stuntguard.utils.adapters.mission

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.project.stuntguard.data.remote.response.DataMission
import id.project.stuntguard.databinding.ItemMissionBinding

class MissionListAdapter : ListAdapter<DataMission, MissionListAdapter.ListViewHolder>(
    DIFF_CALLBACK
) {
    private lateinit var onClickCallback: OnClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemMissionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataMission = getItem(position)

        holder.apply {
            bind(dataMission)
            binding.delete.setOnClickListener {
                onClickCallback.onDeleteClicked(dataMission.id)
            }
        }
    }

    class ListViewHolder(val binding: ItemMissionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataMission: DataMission) {
            binding.apply {
                missionTitle.text = dataMission.title
                missionDescription.text = dataMission.description
            }
        }
    }

    fun setOnItemClickCallback(onClickCallback: OnClickCallback) {
        this.onClickCallback = onClickCallback
    }

    interface OnClickCallback {
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