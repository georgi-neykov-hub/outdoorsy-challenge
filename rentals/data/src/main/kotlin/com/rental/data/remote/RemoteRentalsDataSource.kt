package com.rental.data.remote

import com.rental.data.requireValidPaginationParameters
import com.rental.model.RentalEntry

internal class RemoteRentalsDataSource(
    private val rentalsApi: RentalsApi
) : RentalsDataSource {
    override suspend operator fun invoke(
        pageLimit: Int?,
        pageOffset: Int?,
        address: String?,
        query: String?
    ): List<RentalEntry> {
        requireValidPaginationParameters(pageLimit, pageOffset)
        return rentalsApi.listRentals((pageLimit), pageOffset, address, query).entries
    }
}