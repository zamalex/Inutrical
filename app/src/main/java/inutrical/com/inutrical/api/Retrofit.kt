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
    }).addInterceptor(logging).connectTimeout(30,TimeUnit.SECONDS).build()


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.inutrical.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val Api: ApiService by lazy {
        retrofit.create(
            ApiService::class.java
        )
    }
}