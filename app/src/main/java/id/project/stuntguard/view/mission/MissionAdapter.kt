package id.project.stuntguard.view.mission

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.project.stuntguard.R
import id.project.stuntguard.data.model.MissionModel
import id.project.stuntguard.data.remote.response.DataChild
import id.project.stuntguard.data.remote.response.DataMission
import id.project.stuntguard.databinding.ItemHistoryChildBinding
import id.project.stuntguard.databinding.ItemMissionBinding
import id.project.stuntguard.utils.adapters.history.ChildListAdapter

class MissionAdapter : ListAdapter<DataMission, MissionAdapter.MissionViewHolder>(
    MissionAdapter.DIFF_CALLBACK
) {
    private lateinit var onClickCallback: OnClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {
        val binding = ItemMissionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MissionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        val dataMission = getItem(position)
        holder.apply {
            bind(dataMission)
            itemView.setOnClickListener {
                onClickCallback.onItemClicked(dataMission.id, dataMission.title)
            }
            binding.delete.setOnClickListener {
                onClickCallback.onDeleteClicked(dataMission.id)
            }
        }
    }

    class MissionViewHolder(val binding: ItemMissionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataMission: DataMission) {
            binding.apply {
                missionTitle.text = dataMission.title
                missionDescription.text = dataMission.description
            }
        }
    }

    fun setOnItemClickCallback(onClickCallback: MissionAdapter.OnClickCallback) {
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
