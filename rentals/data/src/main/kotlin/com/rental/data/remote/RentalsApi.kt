package com.rental.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

internal interface RentalsApi {

    @GET("/rentals?raw_json=true")
    suspend fun listRentals(
        @Query("page[limit]") pageLimit: Int? = null,
        @Query("page[offset]") pageOffset: Int? = null,
        @Query("address") address: String? = null,
        query: String? = null,
    ): RentalsListResult
}