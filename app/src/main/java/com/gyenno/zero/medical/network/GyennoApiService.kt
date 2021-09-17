package com.gyenno.zero.medical.network

import com.gyenno.zero.medical.entity.JiGuangPushBody
import com.gyenno.zero.medical.entity.JiGuangPushResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException

/**
 * Created by Chon Den.
 *
 * @date 2021/09/17
 */
interface GyennoApiService {

    @POST("v3/push")
    @Headers("Authorization: Basic Mzg2MDlmMTU3ZTljMTlmOTE2ZDExMzE3OjkwMzk0NzViZWI5NDVjNTZjZGI0MTkzYg==")
    suspend fun pushMessage(@Body body: JiGuangPushBody): JiGuangPushResponse
}


object GyennoApi {
    val retrofitService: GyennoApiService by lazy {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor {
                Timber.tag("OkHttp").d(it)
            }.apply { level = HttpLoggingInterceptor.Level.BODY })
            .eventListener(object : EventListener() {
                override fun callFailed(call: Call, ioe: IOException) {
                    Timber.e(ioe)
                    if (ioe is UnknownHostException) {
                        // TODO No Internet Connection, Check Please!
                    }
                }
            })
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.jpush.cn/")
            .client(okHttpClient)
            .build()
        retrofit.create(GyennoApiService::class.java)
    }
}
