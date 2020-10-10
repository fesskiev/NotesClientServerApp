package com.fesskiev.compose.data.remote

import com.fesskiev.Routes.LOGIN
import com.fesskiev.Routes.LOGOUT
import com.fesskiev.Routes.REGISTRATION
import com.fesskiev.model.JWTAuth
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class AppInterceptor : Interceptor {

    private var jwtAuth: JWTAuth? = null
    private var session: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        val builder = request.newBuilder()
        builder.addHeader("Content-Type", "application/json")
        jwtAuth?.token?.let { token ->
            builder.addHeader("Authorization", "Bearer $token")
        }
        session?.let { session ->
            builder.addHeader("Session", session)
        }
        val response = chain.proceed(builder.build())
        val contentType = response.body?.contentType()
        val body = response.body?.string()
        if (body != null) {
            when {
                url.endsWith(REGISTRATION) || url.endsWith(LOGIN) -> jwtAuth = parseJWT(body)
                url.endsWith(LOGOUT) -> jwtAuth = null
            }
        }
        if (response.isSuccessful) {
            session = response.headers["Session"]
        }
        return response.newBuilder()
            .body(body?.toResponseBody(contentType))
            .build()
    }
}