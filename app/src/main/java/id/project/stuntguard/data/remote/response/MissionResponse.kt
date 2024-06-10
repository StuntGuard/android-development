package id.project.stuntguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class MissionResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data: List<DataMission>
)

data class DataMission(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("assignedToUser")
    val assignedToUser: Int,

    @field:SerializedName("assignedToChild")
    val assignedToChild: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)