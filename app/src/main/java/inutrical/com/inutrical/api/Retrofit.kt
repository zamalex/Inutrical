package inutrical.com.inutrical.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

object Retrofit {


    val logging: HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    val client: OkHttpClient = OkHttpClient.Builder().hostnameVerifier(object : HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
            return true
        }
    }).addInterceptor { chain ->
        val request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${LocalData.token}").build()
        chain.proceed(request)
    }.addInterceptor(logging).connectTimeout(60,TimeUnit.SECONDS).readTimeout(60,TimeUnit.SECONDS).writeTimeout(60,TimeUnit.SECONDS).build()


    private val retrofit = Retrofit.Builder()
        .baseUrl("http://inutrical.badee.com.sa/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val Api: ApiService by lazy {
        retrofit.create(
            ApiService::class.java
        )
    }
}