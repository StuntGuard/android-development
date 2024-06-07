package id.project.stuntguard.utils.adapters.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.project.stuntguard.data.remote.response.DataChild
import id.project.stuntguard.databinding.ItemHistoryChildBinding

class ChildListAdapter : ListAdapter<DataChild, ChildListAdapter.ListViewHolder>(
    DIFF_CALLBACK
) {
    private lateinit var onClickCallback: OnClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemHistoryChildBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataChild = getItem(position)
        holder.apply {
            bind(dataChild)
            itemView.setOnClickListener {
                onClickCallback.onItemClicked(dataChild.id, dataChild.name)
            }
            binding.delete.setOnClickListener {
                onClickCallback.onDeleteClicked(dataChild.id)
            }
        }
    }

    class ListViewHolder(val binding: ItemHistoryChildBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataChild: DataChild) {
            binding.apply {
                Glide.with(profileImage.context)
                    .load(dataChild.urlPhoto)
                    .circleCrop()
                    .into(profileImage)
                profileName.text = dataChild.name
                profileGender.text = dataChild.gender
            }
        }
    }

    fun setOnItemClickCallback(onClickCallback: OnClickCallback) {
        this.onClickCallback = onClickCallback
    }

    interface OnClickCallback {
        fun onItemClicked(idChild: Int, childName: String)
        fun onDeleteClicked(idChild: Int)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataChild>() {
            override fun areItemsTheSame(
                oldItem: DataChild,
                newItem: DataChild
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataChild,
                newItem: DataChild
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}