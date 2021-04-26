package io.techmeskills.an02onl_plannerapp.cloud

import io.techmeskills.an02onl_plannerapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IRetrofitSettings {
    @GET("importNotes")
    suspend fun importNotes(
        @Query("userName") accountName: String,
        @Query("phoneId") phoneId: String
    ): Response<List<CloudNote>>

    @POST("exportNotes")
    suspend fun exportNotes(
        @Body body: ExportNotesRequestBody
    ): Response<Any?>

    companion object {
        const val URL = "https://us-central1-plannerapi-2d0bf.cloudfunctions.net"

        fun get(): IRetrofitSettings = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL)
            .client(
                OkHttpClient.Builder().apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    }
                }.build()
            )
            .build().create(IRetrofitSettings::class.java)
    }
}