package mr.fugugames.com.transformers.httpService.auth

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor (private val tokenHandler: TokenHandler): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenHandler.getToken()
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }

}