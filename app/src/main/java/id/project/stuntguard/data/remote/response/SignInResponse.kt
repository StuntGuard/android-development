package id.project.stuntguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("name")
    val name: String
)
