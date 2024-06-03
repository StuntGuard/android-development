package id.project.stuntguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
