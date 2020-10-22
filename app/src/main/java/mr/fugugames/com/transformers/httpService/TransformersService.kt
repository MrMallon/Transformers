package mr.fugugames.com.transformers.httpService

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import mr.fugugames.com.transformers.BuildConfig
import mr.fugugames.com.transformers.httpService.auth.TokenHandler
import mr.fugugames.com.transformers.httpService.auth.TokenInterceptor
import mr.fugugames.com.transformers.models.Transformers
import mr.fugugames.com.transformers.models.TransformersData
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface TransformersService {

    @POST("transformers")
    fun createNewTransformerAsync(@Body transformer: Transformers): Deferred<Transformers>

    @PUT("transformers")
    fun updateTransformerAsync(@Body transformer: Transformers): Deferred<Transformers>

    @DELETE("transformers/{id}")
    fun deleteTransformerById(@Path("id") id:String): Deferred<TransformersData>

    @GET("/transformers")
    fun getTransformersAsync(): Deferred<TransformersData>

    companion object {
        operator fun invoke(tokenHandler: TokenHandler) : TransformersService {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(TokenInterceptor(tokenHandler))
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.API_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TransformersService::class.java)
        }
    }
}