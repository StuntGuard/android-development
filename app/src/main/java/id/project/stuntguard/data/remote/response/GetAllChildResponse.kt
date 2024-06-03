package id.project.stuntguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetAllChildResponse(

	@field:SerializedName("data")
	val data: List<DataChild>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataChild(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("assignedToUser")
	val assignedToUser: Int,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("url_photo")
	val urlPhoto: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
