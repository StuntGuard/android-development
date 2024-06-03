package id.project.stuntguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictChildResponse(

	@field:SerializedName("data")
	val data: DataPredict,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataPredict(

	@field:SerializedName("confidenceScore")
	val confidenceScore: Any,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("label")
	val label: String
)
