package mr.fugugames.com.transformers.httpService.auth

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import mr.fugugames.com.transformers.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface AuthService{
    @GET("allspark")
    fun getNewToken(): Call<String>

    companion object {
        operator fun invoke() : AuthService {

            val okHttpClient = OkHttpClient.Builder()
                .build()
            val gson = GsonBuilder()
                .setLenient()
                .create()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.API_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(AuthService::class.java)
        }
    }
}