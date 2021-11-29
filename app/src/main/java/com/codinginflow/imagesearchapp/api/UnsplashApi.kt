package com.codinginflow.imagesearchapp.api

import com.codinginflow.imagesearchapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface UnsplashApi {

    // TODO Здесь companion object по сути не нужен
    companion object {
        // TODO это надо вынести в gradle файл, так жк как ты сделала с UNSPLASH_ACCESS_KEY
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }

    // TODO "Authorization: Client-ID $CLIENT_ID" здесь просто можно сделать BuildConfig.UNSPLASH_ACCESS_KEY
    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UnsplashResponse
}