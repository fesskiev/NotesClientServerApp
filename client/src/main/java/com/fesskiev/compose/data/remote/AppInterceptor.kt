package com.fesskiev.compose.data.remote

import com.fesskiev.Headers.AUTHORIZATION
import com.fesskiev.Headers.CONTENT_TYPE
import com.fesskiev.Headers.SESSION
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
        builder.addHeader(CONTENT_TYPE, "application/json")
        jwtAuth?.token?.let { token ->
            builder.addHeader(AUTHORIZATION, "Bearer $token")
        }
        session?.let { session ->
            builder.addHeader(SESSION, session)
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
            session = response.headers[SESSION]
        }
        return response.newBuilder()
            .body(body?.toResponseBody(contentType))
            .build()
    }
}