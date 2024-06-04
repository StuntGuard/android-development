package id.project.stuntguard.view.mission

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.project.stuntguard.R
import id.project.stuntguard.data.preferences.MissionModel

class MissionAdapter(private val missionList: List<MissionModel>) :
    RecyclerView.Adapter<MissionAdapter.MissionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mission, parent, false)
        return MissionViewHolder(view)
    }

    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        val mission = missionList[position]
        holder.bind(mission)
    }

    override fun getItemCount(): Int = missionList.size

    class MissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val missionTitle: TextView = itemView.findViewById(R.id.mission_title)
        private val missionDescription: TextView = itemView.findViewById(R.id.mission_description)

        fun bind(mission: MissionModel) {
            missionTitle.text = mission.title
            missionDescription.text = mission.description
        }
    }
}
