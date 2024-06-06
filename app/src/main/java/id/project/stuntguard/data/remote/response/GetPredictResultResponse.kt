package id.project.stuntguard.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetPredictResultResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("score")
	val score: Any,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("reccomendations")
	val recommendations: List<RecommendationsItem>,

	@field:SerializedName("subtitle")
	val subtitle: String,

	@field:SerializedName("prediction")
	val prediction: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("message")
	val message: String
)

data class RecommendationsItem(

	@field:SerializedName("assignedToPredict")
	val assignedToPredict: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String
)
