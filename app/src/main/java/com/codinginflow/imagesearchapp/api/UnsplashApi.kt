package com.codinginflow.imagesearchapp.api


import com.codinginflow.imagesearchapp.BuildConfig.UNSPLASH_ACCESS_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {


    @Headers("Accept-Version: v1", "Authorization: Client-ID $UNSPLASH_ACCESS_KEY")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UnsplashResponse
}