package id.project.stuntguard.data.remote.api

import id.project.stuntguard.data.remote.response.SignInResponse
import id.project.stuntguard.data.remote.response.SignUpResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}