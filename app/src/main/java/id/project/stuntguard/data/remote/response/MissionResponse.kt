package id.project.stuntguard.data.remote.response

import com.google.gson.annotations.SerializedName
import id.project.stuntguard.data.preferences.UserModel

data class MissionResponse (
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data: DataMission
)

data class DataMission (
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("assignedToUser")
    val assignedToUserModel: UserModel,

    @field:SerializedName("assignedToChild")
    val assignedToChild: DataChild
)