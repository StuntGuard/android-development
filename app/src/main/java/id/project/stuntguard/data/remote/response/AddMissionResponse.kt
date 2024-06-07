package id.project.stuntguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddMissionResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
