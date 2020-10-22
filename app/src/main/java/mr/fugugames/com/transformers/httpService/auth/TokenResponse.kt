package mr.fugugames.com.transformers.httpService.auth

data class TokenResponse(val accessToken: String,
                         val expiresIn: Long)