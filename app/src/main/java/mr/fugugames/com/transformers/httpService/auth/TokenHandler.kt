package mr.fugugames.com.transformers.httpService.auth

import android.content.Context
import com.google.gson.Gson
import java.util.*

interface TokenHandler {
    fun refreshToken(): TokenResponse
    fun getToken(): String
    fun storeToken(token: TokenResponse)
}

class TokenHandlerImpl(private val context: Context, private val apiService: AuthService): TokenHandler {
    override fun refreshToken(): TokenResponse {
        val response = apiService.getNewToken().execute()

        if(!response.isSuccessful)
            return TokenResponse("", 0)

        val result = response.body()
        val cal = getTimeCal()
        cal.add(Calendar.HOUR, 1)
        val expiresIn = cal.timeInMillis
        return TokenResponse(result?:"", expiresIn)
    }

    override fun getToken(): String {
        val sharedPref = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        val json = sharedPref.getString("USER_TOKEN", "")

        val token = if (json.isNullOrEmpty()) {
            refreshToken()
        } else {
            Gson().fromJson(json, TokenResponse::class.java)
        }
        return if(token.expiresIn < getTimeCal().timeInMillis){
            val newToken = refreshToken()
            storeToken(newToken)
            newToken.accessToken
        }
        else
            token.accessToken
    }

    override fun storeToken(token: TokenResponse) {
        val sharedPref = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString("USER_TOKEN", Gson().toJson(token))
            apply()
        }
    }

    private fun getTimeCal():Calendar{
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }
}