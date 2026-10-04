package com.krishna.spirecart

import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 30,
        @Query("skip") skip: Int = 0
    ): ProductResponse

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int = 30,
        @Query("skip") skip: Int = 0
    ): ProductResponse
}