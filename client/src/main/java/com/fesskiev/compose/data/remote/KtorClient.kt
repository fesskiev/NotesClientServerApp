package com.fesskiev.compose.data.remote

import android.util.Log
import com.fesskiev.Headers.AUTHORIZATION
import com.fesskiev.Headers.CONTENT_TYPE
import com.fesskiev.Headers.SESSION
import com.fesskiev.Routes.LOGIN
import com.fesskiev.Routes.LOGOUT
import com.fesskiev.Routes.REGISTRATION
import com.fesskiev.compose.BuildConfig
import com.fesskiev.compose.data.remote.exceptions.BadRequestException
import com.fesskiev.compose.data.remote.exceptions.NoNetworkException
import com.fesskiev.compose.data.remote.exceptions.UnauthorizedException
import com.fesskiev.compose.ui.utils.NetworkManager
import com.fesskiev.model.JWTAuth
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

fun provideKtorClient(networkManager: NetworkManager): HttpClient = HttpClient(OkHttp) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.v("Ktor", message)
            }
        }
        level = LogLevel.ALL
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 15000L
        connectTimeoutMillis = 15000L
        socketTimeoutMillis = 15000L
    }
    defaultRequest {
        host = BuildConfig.HOST
        port = BuildConfig.PORT
    }
    engine {
        addInterceptor(AppInterceptor(networkManager))
    }
}

class AppInterceptor(private val networkManager: NetworkManager) : Interceptor {

    private var jwtAuth: JWTAuth? = null
    private var session: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkManager.isNetworkAvailable) {
            throw NoNetworkException()
        }
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
        val body = response.body?.string()
        val contentType = response.body?.contentType()
        if (body != null) {
            when (response.code) {
                400 -> throw BadRequestException(body.parseServerErrorCode())
                401 -> throw UnauthorizedException(body.parseServerErrorCode())
                else -> when {
                    url.endsWith(REGISTRATION) || url.endsWith(LOGIN) -> jwtAuth = body.parseJWT()
                    url.endsWith(LOGOUT) -> jwtAuth = null
                }
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