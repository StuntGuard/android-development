package id.project.stuntguard.data.remote.api

import id.project.stuntguard.data.remote.response.GetAllChildResponse
import id.project.stuntguard.data.remote.response.GetPredictHistoryResponse
import id.project.stuntguard.data.remote.response.GetPredictResultResponse
import id.project.stuntguard.data.remote.response.PredictChildResponse
import id.project.stuntguard.data.remote.response.SignInResponse
import id.project.stuntguard.data.remote.response.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    @POST("predict/{idChild}")
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

    // History Section
    @GET("history/{idChild")
    suspend fun getPredictHistory(
        @Header("Authorization") token: String,
        @Path("idChild") idChild: Int
    ): GetPredictHistoryResponse
}