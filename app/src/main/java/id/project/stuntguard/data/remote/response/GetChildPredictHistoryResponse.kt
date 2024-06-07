package id.project.stuntguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetChildPredictHistoryResponse(

	@field:SerializedName("data")
	val data: List<DataHistory>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataHistory(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("subtitle")
	val subtitle: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("prediction")
	val prediction: String,

	@field:SerializedName("id")
	val id: Int
)
