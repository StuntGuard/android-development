package id.project.stuntguard.data.remote.api

import id.project.stuntguard.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private val loggingInterceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Main Api Service :
    fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://web-service-i5khuj4mnq-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    // Reset Password Api Service :
    fun getResetPasswordApiService(): ResetPasswordApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://email-service-i5khuj4mnq-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ResetPasswordApiService::class.java)
    }
}