package id.project.stuntguard.data.remote.api

import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.data.remote.response.GetChildPredictHistoryResponse
import id.project.stuntguard.data.remote.response.GetPredictResultResponse
import id.project.stuntguard.data.remote.response.MissionResponse
import id.project.stuntguard.data.remote.response.PredictChildResponse
import id.project.stuntguard.data.remote.response.SignInResponse
import id.project.stuntguard.data.remote.response.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("sign-up")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignUpResponse

    @FormUrlEncoded
    @POST("sign-in")
    suspend fun signIn(
        @Field("email") email: String,
        @Field("password") password: String,
    ): SignInResponse

    @GET("childs")
    suspend fun getAllChild(
        @Header("Authorization") token: String
    ): GetAllChildResponse

    // Return response is using SignUpResponse cause it return same value as DeleteChildResponse
    @DELETE("childs/{idChild}")
    suspend fun deleteChild(
        @Header("Authorization") token: String,
        @Path("idChild") idChild: Int
    ): SignUpResponse

    // Analyze Section :
    // Return response is using SignUpResponse cause it return same value as AddNewChildResponse
    @Multipart
    @POST("childs")
    suspend fun addNewChild(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("gender") gender: RequestBody
    ): SignUpResponse

    @FormUrlEncoded
    @POST("predicts/{idChild}")
    suspend fun predict(
        @Header("Authorization") token: String,
        @Path("idChild") idChild: Int,
        @Field("age") age: Int,
        @Field("gender") gender: String,
        @Field("height") height: Double
    ): PredictChildResponse

    @GET("predicts/{idPredict}")
    suspend fun getPredictResult(
        @Header("Authorization") token: String,
        @Path("idPredict") idPredict: Int
    ): GetPredictResultResponse

    // History Section :
    @GET("history/{idChild}")
    suspend fun getPredictHistory(
        @Header("Authorization") token: String,
        @Path("idChild") idChild: Int
    ): GetChildPredictHistoryResponse

    // Mission Section :
    @GET("missions/{idChild}")
    suspend fun getMissions(
        @Header("Authorization") token: String,
        @Path("idChild") idChild: Int
    ): MissionResponse

    @FormUrlEncoded
    @POST("missions/{idChild}")
    suspend fun postMission(
        @Header("Authorization") token: String,
        @Path("idChild") idChild: Int,
        @Field("title") title: String,
        @Field("description") description: String,
    ): SignUpResponse

    @DELETE("missions/{idMission}")
    suspend fun deleteMissions(
        @Header("Authorization") token: String,
        @Path("idMission") idMission: Int
    ): SignUpResponse
}

interface EmailApiService {
    @FormUrlEncoded
    @POST("checkEmail")
    suspend fun newEmailCheck(
        @Field("email") email: String
    ): SignUpResponse

    @FormUrlEncoded
    @POST("checkEmail/verify")
    suspend fun verifyNewEmail(
        @Field("token") token: String
    ): SignUpResponse

    @FormUrlEncoded
    @POST("reset-password")
    suspend fun emailCheck(
        @Field("email") email: String
    ): SignUpResponse

    @FormUrlEncoded
    @POST("reset-password/verify")
    suspend fun verifyEmail(
        @Field("token") token: String
    ): SignUpResponse

    @FormUrlEncoded
    @POST("reset-password/update")
    suspend fun updatePassword(
        @Field("token") token: String,
        @Field("password") password: String
    ): SignUpResponse
}