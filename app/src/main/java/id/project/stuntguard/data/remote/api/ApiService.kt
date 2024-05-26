package id.project.stuntguard.data.remote.api

import id.project.stuntguard.data.remote.response.SigninResponse
import id.project.stuntguard.data.remote.response.SignupResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("sign-up")
    fun signup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignupResponse>

    @FormUrlEncoded
    @POST("sign-in")
    suspend fun signin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<SigninResponse>
}